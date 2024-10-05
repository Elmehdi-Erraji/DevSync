package web.controller.user;

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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/tasks")
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

    // Display tasks (both assigned to user and self-created)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Redirect to login if no user session
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        if (action == null) {
            // Fetch both tasks assigned to user and tasks created by user
            List<Task> assignedTasks = taskService.findTasksAssignedToUser((long) loggedInUser.getId());
            List<Task> selfCreatedTasks = taskService.findTasksCreatedByUser((long) loggedInUser.getId());

            // Set the task lists in request attributes
            request.setAttribute("assignedTasks", assignedTasks);
            request.setAttribute("selfCreatedTasks", selfCreatedTasks);
            request.getRequestDispatcher("/views/dashboard/user/tasks/home.jsp").forward(request, response);

        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Task task = taskService.findTaskById(id);
            request.setAttribute("task", task);

            // Get all tags for the dropdown
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("tags", tags);

            request.getRequestDispatcher("/views/dashboard/user/tasks/edit.jsp").forward(request, response);

        } else if (action.equals("create")) {
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/views/dashboard/user/tasks/create.jsp").forward(request, response);
        }
    }

    // Handle creating and updating tasks
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        // Debugging: Print method and id
        System.out.println("Method: " + method);
        System.out.println("ID: " + id);

        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                // Debugging: Print before deletion
                System.out.println("Deleting task with ID: " + id);
                taskService.deleteTask(Long.parseLong(id));
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("No ID provided for deletion.");
            }
        } else {
            // Collect task fields from the request
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDate = request.getParameter("dueDate");
            String statusParam = "NEW"; // Assuming default status is NEW
            User loggedInUser = (User) request.getSession().getAttribute("user"); // Logged-in user is the creator

            // Print the parameters to the log for debugging
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);
            System.out.println("Due Date: " + dueDate);
            System.out.println("Status: " + statusParam);
            System.out.println("Logged In User: " + (loggedInUser != null ? loggedInUser.getUsername() : "No user logged in"));

            // Handle tags selection
            String[] tagIds = request.getParameterValues("tags");

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
                System.out.println("Task status set to: " + status);
            }

            // Set the creator (the logged-in user)
            task.setCreator(loggedInUser);

            // Set tags
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag tag = tagService.findTagById(Long.parseLong(tagId));
                    task.getTags().add(tag);
                    System.out.println("Added tag: " + tag.getName() + " (ID: " + tagId + ")");
                }
            }

            // Handle create and update operations
            if (id != null && !id.isEmpty()) {
                task.setId(Long.parseLong(id));
                System.out.println("Updating task with ID: " + id);
                taskService.updateTask(task);
                System.out.println("Task updated successfully.");
            } else {
                System.out.println("Creating new task.");
                taskService.insertTask(task);
                System.out.println("New task created successfully.");
            }
        }

        response.sendRedirect("tasks?status=success"); // Redirect after processing
    }
}
