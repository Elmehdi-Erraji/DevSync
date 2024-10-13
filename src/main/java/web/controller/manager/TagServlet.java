package web.controller.manager;

import domain.Tag;
import service.TagService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

@WebServlet("/manager/tags")
public class TagServlet extends HttpServlet {
    private TagService tagService;
    private ValidatorFactory validatorFactory;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        try {
            tagService = new TagService();
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
            List<Tag> tagList = tagService.findAllTags();
            request.setAttribute("tags", tagList);
            request.getRequestDispatcher("/views/dashboard/manager/tags/home.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Tag tag = tagService.findTagById(id);
            request.setAttribute("tag", tag);
            request.getRequestDispatcher("/views/dashboard/manager/tags/edit.jsp").forward(request, response);
        } else if (action.equals("create")) {
            request.getRequestDispatcher("/views/dashboard/manager/tags/create.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                tagService.deleteTag(Long.parseLong(id));
            }
        } else {
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

        response.sendRedirect(request.getContextPath() + "/manager/tags?status=success"); // Redirect after processing
    }
}
