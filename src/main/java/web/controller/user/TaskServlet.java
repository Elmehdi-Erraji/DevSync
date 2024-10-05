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

        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                taskService.deleteTask(Long.parseLong(id));
            }
        } else {
            // Collect task fields from the request
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDate = request.getParameter("dueDate");
            String statusParam = request.getParameter("status");
            Long creatorId = Long.parseLong(request.getParameter("creator")); // Assuming creator is selected
            Long assignedUserId = Long.parseLong(request.getParameter("assignedUser")); // Assuming assigned user is selected
            String[] tagIds = request.getParameterValues("tags"); // Assuming tags are selected as checkbox or multi-select




            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);

            // Set the status
            if (statusParam != null) {
                TaskStatus status = TaskStatus.valueOf(statusParam.toUpperCase());
                task.setStatus(status);
            }

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
