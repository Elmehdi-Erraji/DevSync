package controller.manager;

import model.Tag;
import service.TagService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/manager/tags")
public class TagServlet extends HttpServlet {
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        try {
            tagService = new TagService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TagServlet", e);
        }
    }

    // Display all tags
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

    // Handle creating and updating tags
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        // Handle delete request
        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                tagService.deleteTag(Long.parseLong(id));
            }
        } else {
            // Common fields for both create and update
            String name = request.getParameter("name");

            // Check for null values
            if (name == null || name.isEmpty()) {
                throw new ServletException("Tag name is required");
            }

            Tag tag = new Tag();
            tag.setName(name);

            // Handle create and update operations
            if (id != null && !id.isEmpty()) {
                tag.setId(Long.parseLong(id));
                tagService.updateTag(tag);
            } else {
                tagService.insertTag(tag);
            }
        }

        response.sendRedirect("tags?status=success"); // Redirect after processing
    }
}
