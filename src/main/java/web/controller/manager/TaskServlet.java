package web.controller.manager;

import domain.Task;
import domain.Tag;
import domain.User;
import domain.enums.TaskStatus;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import service.TaskService;
import service.TagService;
import service.UserService;
import validation.TaskValidation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
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
            int tasksCount = taskList.size();

            request.setAttribute("tasksCount", tasksCount);
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
            String startDateParam = request.getParameter("startDate");
            String dueDateParam = request.getParameter("dueDate");


            Long creatorId = Long.parseLong(request.getParameter("creator"));
            Long assignedUserId = Long.parseLong(request.getParameter("assignedUser"));
            String[] tagIds = request.getParameterValues("tags");

            LocalDate startDate = LocalDate.parse(startDateParam);
            LocalDate dueDate = LocalDate.parse(dueDateParam);

            if (!TaskValidation.isValidTitle(title)) {
                request.setAttribute("error", "Title must not be empty and cannot exceed 100 characters.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            if (!TaskValidation.isValidDescription(description)) {
                request.setAttribute("error", "Description must not be empty and cannot exceed 500 characters.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            if (!TaskValidation.isValidUser(creatorId)) {
                request.setAttribute("error", "A valid creator must be selected.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            if (!TaskValidation.isValidUser(assignedUserId)) {
                request.setAttribute("error", "A valid assigned user must be selected.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            if (!TaskValidation.isValidTags(tagIds)) {
                request.setAttribute("error", "At least one tag must be selected.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            if (!TaskValidation.isValidStartDate(startDate)) {
                request.setAttribute("error", "Start date must be at least 3 days ahead of today.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            if (!TaskValidation.isValidDueDate(startDate, dueDate)) {
                request.setAttribute("error", "Due date must be at least one day after the start date.");
                loadUsersAndTags(request);  
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
                return;
            }

            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setStartDate(startDate);
            task.setDueDate(String.valueOf(dueDate));

            task.setStatus(TaskStatus.NEW);


            User creator = userService.findUserById(creatorId);
            User assignedUser = userService.findUserById(assignedUserId);
            task.setCreator(creator);
            task.setAssignedUser(assignedUser);

            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag tag = tagService.findTagById(Long.parseLong(tagId));
                    task.getTags().add(tag);
                }
            }


            if (id != null && !id.isEmpty()) {
                task.setId(Long.parseLong(id));
                taskService.updateTask(task);
            } else {
                taskService.insertTask(task);
            }
        }

        response.sendRedirect("tasks?status=success");
    }

    private void loadUsersAndTags(HttpServletRequest request) {
        List<User> users = userService.findAllUsers();
        List<Tag> tags = tagService.findAllTags();
        request.setAttribute("users", users);
        request.setAttribute("tags", tags);
    }
}
