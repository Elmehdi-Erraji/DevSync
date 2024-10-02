package controller.manager;

import model.Task;
import model.Tag;
import model.User;
import model.enums.TaskStatus;
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

    // Display all tasks
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

            // Get all users and tags for the dropdowns
            List<User> users = userService.findAllUsers();
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("users", users);
            request.setAttribute("tags", tags);

            request.getRequestDispatcher("/views/dashboard/manager/tasks/edit.jsp").forward(request, response);
        } else if (action.equals("create")) {
            // Get all users and tags for the dropdowns
            List<User> users = userService.findAllUsers();
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("users", users);
            request.setAttribute("tags", tags);

            request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
        }
    }

    // Handle creating and updating tasks
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                taskService.deleteTask(Long.parseLong(id));
            }
        } else {
            // Collect task fields from the request
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDate = request.getParameter("dueDate");
            String statusParam = "NEW";
            Long creatorId = Long.parseLong(request.getParameter("creator")); // Assuming creator is selected
            Long assignedUserId = Long.parseLong(request.getParameter("assignedUser")); // Assuming assigned user is selected
            String[] tagIds = request.getParameterValues("tags"); // Assuming tags are selected as checkbox or multi-select

            // Print the parameters to the log for debugging
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            System.out.println("Due Date: " + dueDate);
            System.out.println("Status: " + statusParam);
            System.out.println("Creator ID: " + creatorId);
            System.out.println("Assigned User ID: " + assignedUserId);

            if (tagIds != null) {
                System.out.println("Selected Tags:");
                for (String tagId : tagIds) {
                    System.out.println("Tag ID: " + tagId);
                }
            } else {
                System.out.println("No tags selected.");
            }
            // Create a new Task object
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
