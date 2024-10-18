package web.controller.manager;

import domain.Tag;
import repository.TagRepository;
import service.TagService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@WebServlet("/manager/tags")
public class TagServlet extends HttpServlet {
    private TagService tagService;
    private ValidatorFactory validatorFactory;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        try {
            tagService = new TagService(new TagRepository());
            validatorFactory = Validation.buildDefaultValidatorFactory();
            validator = validatorFactory.getValidator();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TagServlet", e);
        }
    }

    @Override
    public void destroy() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            handleListTags(request, response);
        } else if (action.equals("edit")) {
            handleEditTag(request, response);
        } else if (action.equals("create")) {
            handleCreateTag(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void handleListTags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<List<Tag>> tagListOptional = tagService.findAllTags();
        List<Tag> tagList = tagListOptional.orElse(List.of());

        request.setAttribute("tagsCount", tagList.size());
        request.setAttribute("tags", tagList);
        request.getRequestDispatcher("/views/dashboard/manager/tags/home.jsp").forward(request, response);
    }

    private void handleEditTag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Tag> tagInfo = tagService.findTagById(id);

            if (tagInfo.isPresent()) {
                request.setAttribute("tag", tagInfo.get());
                request.getRequestDispatcher("/views/dashboard/manager/tags/edit.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tag not found");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tag ID format");
        }
    }

    private void handleCreateTag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/dashboard/manager/tags/create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(method)) {
                deleteTag(id);
            } else {
                handleTagCreationOrUpdate(request, id, response);
            }
            response.sendRedirect(request.getContextPath() + "/manager/tags?status=success");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/views/dashboard/manager/tags/create.jsp").forward(request, response);
        }
    }

    private void deleteTag(String id) {
        if (id != null && !id.isEmpty()) {
            tagService.deleteTag(Long.parseLong(id));
        }
    }

    private void handleTagCreationOrUpdate(HttpServletRequest request, String id, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        Tag tag = new Tag();
        tag.setName(name);

        Set<ConstraintViolation<Tag>> violations = validator.validate(tag);

        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Tag> violation : violations) {
                errorMessages.append(violation.getMessage()).append("<br>");
            }
            request.setAttribute("errorMessages", errorMessages.toString());
            request.getRequestDispatcher("/views/dashboard/manager/tags/create.jsp").forward(request, response);
            return;
        }

        if (id != null && !id.isEmpty()) {
            tag.setId(Long.parseLong(id));
            tagService.updateTag(tag);
        } else {
            tagService.insertTag(tag);
        }
    }
}
