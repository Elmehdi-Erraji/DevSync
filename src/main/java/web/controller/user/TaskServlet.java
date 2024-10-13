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
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Task task = taskService.findTaskById(id);
            request.setAttribute("task", task);

            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/views/dashboard/user/tasks/edit.jsp").forward(request, response);
        } else if (action.equals("statusUpdate")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Task task = taskService.findTaskById(id);
            request.setAttribute("task", task);
            request.getRequestDispatcher("/views/dashboard/user/tasks/statusUpdate.jsp").forward(request, response);
        } else if (action.equals("create")) {
            List<Tag> tags = tagService.findAllTags();
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/views/dashboard/user/tasks/create.jsp").forward(request, response);
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
        } else if ("statusUpdate".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                Task task = taskService.findTaskById(Long.parseLong(id));
                String statusParam = request.getParameter("status");
                if (statusParam != null && !statusParam.isEmpty()) {
                    TaskStatus status = TaskStatus.valueOf(statusParam.toUpperCase());
                    task.setStatus(status);
                    taskService.updateTask(task);
                }
            }
        } else {
            if (id != null && !id.isEmpty()) {
                Long taskId = Long.parseLong(id);
                Task task = taskService.findTaskById(taskId);

                updateTaskFromRequest(task, request);
                taskService.updateTask(task);
            } else {
                Task newTask = new Task();
                updateTaskFromRequest(newTask, request);
                newTask.setStatus(TaskStatus.NEW);
                taskService.insertTask(newTask);
            }
        }

        response.sendRedirect("tasks?status=success");
    }

    private void updateTaskFromRequest(Task task, HttpServletRequest request) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDate = request.getParameter("dueDate");
        Long creatorId = Long.parseLong(request.getParameter("creator"));
        Long assignedUserId = Long.parseLong(request.getParameter("assignedUser"));
        String statusParam = request.getParameter("status"); // Get the status from the request
        String[] tagIds = request.getParameterValues("tags");

        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);

        if (statusParam != null && !statusParam.isEmpty()) {
            TaskStatus status = TaskStatus.valueOf(statusParam.toUpperCase());
            task.setStatus(status);
        }

        User creator = userService.findUserById(creatorId);
        User assignedUser = userService.findUserById(assignedUserId);
        task.setCreator(creator);
        task.setAssignedUser(assignedUser);

        if (tagIds != null) {
            task.getTags().clear();
            for (String tagId : tagIds) {
                Tag tag = tagService.findTagById(Long.parseLong(tagId));
                task.getTags().add(tag);
            }
        }
    }

}
