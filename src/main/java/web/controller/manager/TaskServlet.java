package web.controller.manager;

import domain.Task;
import domain.Tag;
import domain.User;
import domain.enums.TaskStatus;
import service.TaskService;
import service.TagService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/manager/tasks")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private UserService userService;
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        try {
            taskService = new TaskService();
            userService = new UserService();
            tagService = new TagService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TaskServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            List<Task> taskList = taskService.findAllTasks();
            request.setAttribute("tasks", taskList);
            request.getRequestDispatcher("/views/dashboard/manager/tasks/home.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Task task = taskService.findTaskById(id);
            request.setAttribute("task", task);

            List<User> users = userService.findAllUsers();
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("users", users);
            request.setAttribute("tags", tags);

            request.getRequestDispatcher("/views/dashboard/manager/tasks/edit.jsp").forward(request, response);
        }

        else if (action.equals("create")) {
            List<User> users = userService.findAllUsers();
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("users", users);
            request.setAttribute("tags", tags);

            request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                taskService.deleteTask(Long.parseLong(id));
            }
        } else {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDate = request.getParameter("dueDate");
            String statusParam = "NEW";
            Long creatorId = Long.parseLong(request.getParameter("creator"));
            Long assignedUserId = Long.parseLong(request.getParameter("assignedUser"));
            String[] tagIds = request.getParameterValues("tags");


            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);

            // Set the status
            if (statusParam != null) {
                TaskStatus status = TaskStatus.valueOf(statusParam.toUpperCase());
                task.setStatus(status);
            }

            // Set the creator and assigned user
            User creator = userService.findUserById(creatorId);
            User assignedUser = userService.findUserById(assignedUserId);
            task.setCreator(creator);
            task.setAssignedUser(assignedUser);

            // Set tags
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag tag = tagService.findTagById(Long.parseLong(tagId));
                    task.getTags().add(tag); // This should work now
                }
            } else {
                System.out.println("No tags selected.");
            }

            // Handle create and update operations
            if (id != null && !id.isEmpty()) {
                task.setId(Long.parseLong(id));
                taskService.updateTask(task);
            } else {
                taskService.insertTask(task);
            }
        }

        response.sendRedirect("tasks?status=success"); // Redirect after processing
    }
}
