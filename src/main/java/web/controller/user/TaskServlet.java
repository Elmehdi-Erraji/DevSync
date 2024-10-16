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
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@WebServlet("/user/tasks")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private UserService userService;
    private TagService tagService;
    private ValidatorFactory validatorFactory;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        try {
            taskService = new TaskService();
            userService = new UserService();
            tagService = new TagService();
            validatorFactory = Validation.buildDefaultValidatorFactory();
            validator = validatorFactory.getValidator();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TaskServlet", e);
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        if (action == null) {
            List<Task> assignedTasks = taskService.findTasksAssignedToUser((long) loggedInUser.getId());
            List<Task> selfCreatedTasks = taskService.findTasksCreatedByUser((long) loggedInUser.getId());

            request.setAttribute("assignedTasks", assignedTasks);
            request.setAttribute("selfCreatedTasks", selfCreatedTasks);
            request.getRequestDispatcher("/views/dashboard/user/tasks/home.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            handleEdit(request, response);
        } else if ("create".equals(action)) {
            handleCreate(request, response);
        } else if ("statusUpdate".equals(action)) {
            handleStatusUpdate(request, response);
        }
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Task task = taskService.findTaskById(id);

        Optional<List<Tag>> tags = tagService.findAllTags();
        request.setAttribute("task", task);
        request.setAttribute("tags", tags.orElse(List.of()));
        request.getRequestDispatcher("/views/dashboard/user/tasks/edit.jsp").forward(request, response);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<List<Tag>> tags = tagService.findAllTags();
        request.setAttribute("tags", tags.orElse(List.of()));
        request.getRequestDispatcher("/views/dashboard/user/tasks/create.jsp").forward(request, response);
    }

    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Task task = taskService.findTaskById(id);

        request.setAttribute("task", task);
        request.getRequestDispatcher("/views/dashboard/user/tasks/statusUpdate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        if ("delete".equalsIgnoreCase(method)) {
            handleDelete(request, response);
        } else if ("statusUpdate".equalsIgnoreCase(method)) {
            handleStatusUpdatePost(request, response);
        } else {
            handleSaveOrUpdate(request, response, id);
        }
        response.sendRedirect(request.getContextPath() + "/user/tasks?status=success");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            taskService.deleteTask(Long.parseLong(id));
        }
    }

    private void handleStatusUpdatePost(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            Task task = taskService.findTaskById(Long.parseLong(id));
            String statusParam = request.getParameter("status");

            if (statusParam != null && !statusParam.isEmpty()) {
                try {
                    TaskStatus status = TaskStatus.valueOf(statusParam.toUpperCase());
                    System.out.println("Updating task status to: " + status);
                    task.setStatus(status);
                    taskService.updateTask(task);
                    System.out.println("Task status updated successfully.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status value: " + statusParam);
                }
            } else {
                System.out.println("Status parameter is missing or empty.");
            }
        } else {
            System.out.println("ID parameter is missing or empty.");
        }
    }

    private void handleSaveOrUpdate(HttpServletRequest request, HttpServletResponse response, String id) {
        Task task = (id != null && !id.isEmpty())
                ? taskService.findTaskById(Long.parseLong(id))
                : new Task();

        updateTaskFromRequest(task, request);

        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Task> violation : violations) {
                errorMessages.append(violation.getMessage()).append("<br>");
            }
            request.setAttribute("errorMessages", errorMessages.toString());
            try {
                request.getRequestDispatcher("/views/dashboard/user/tasks/create.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (id != null && !id.isEmpty()) {
            taskService.updateTask(task);
        } else {
            taskService.insertTask(task);
        }
    }

    private void updateTaskFromRequest(Task task, HttpServletRequest request) {
        task.setTitle(request.getParameter("title"));
        task.setDescription(request.getParameter("description"));
        task.setDueDate(request.getParameter("dueDate"));

        Long creatorId = Long.parseLong(request.getParameter("creator"));
        Long assignedUserId = Long.parseLong(request.getParameter("assignedUser"));

        task.setCreator(userService.findUserById(creatorId).orElseThrow(
                () -> new IllegalArgumentException("Creator not found")));
        task.setAssignedUser(userService.findUserById(assignedUserId).orElseThrow(
                () -> new IllegalArgumentException("Assigned user not found")));

        // Update status from request if provided
        String statusParam = request.getParameter("status");
        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                TaskStatus status = TaskStatus.valueOf(statusParam.toUpperCase());
                task.setStatus(status);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + statusParam);
            }
        }

        String[] tagIds = request.getParameterValues("tags");
        if (tagIds != null) {
            task.getTags().clear();
            for (String tagId : tagIds) {
                tagService.findTagById(Long.parseLong(tagId)).ifPresent(task.getTags()::add);
            }
        }
    }
}
