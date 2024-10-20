package web.controller.manager;

import domain.Task;
import domain.Tag;
import domain.User;
import domain.enums.TaskStatus;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import repository.TagRepository;
import repository.UserRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/manager/tasks")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private UserService userService;
    private TagService tagService;

    @Override
    public void init() throws ServletException {
        try {
            taskService = new TaskService();
            userService = new UserService(new UserRepository());
            tagService = new TagService(new TagRepository());
        } catch (Exception e) {
            throw new ServletException("Failed to initialize TaskServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            List<Task> tasks;
            if (action == null || action.equals("filter")) {
                tasks = (action == null) ? taskService.findAllTasks() : filterTasksFromRequest(request);
                setTaskStatistics(request, tasks);  // Reuse the statistics logic
                request.setAttribute("tasks", tasks);
                Optional<List<Tag>> tagListOptional = tagService.findAllTags();
                List<Tag> tagList = tagListOptional.orElse(List.of());
                request.setAttribute("tagList", tagList);
                request.getRequestDispatcher("/views/dashboard/manager/tasks/home.jsp").forward(request, response);

            } else if (action.equals("edit")) {
                Long id = Long.parseLong(request.getParameter("id"));
                request.setAttribute("task", taskService.findTaskById(id));
                request.setAttribute("users", userService.findAllUsers());
                Optional<List<Tag>> tagListOptional = tagService.findAllTags();
                List<Tag> tagList = tagListOptional.orElse(List.of());
                request.setAttribute("tagList", tagList);
                request.getRequestDispatcher("/views/dashboard/manager/tasks/edit.jsp").forward(request, response);

            } else if (action.equals("create")) {
                request.setAttribute("users", userService.findAllUsers());
                Optional<List<Tag>> tagListOptional = tagService.findAllTags();
                List<Tag> tagList = tagListOptional.orElse(List.of());
                request.setAttribute("tagList", tagList);
                request.getRequestDispatcher("/views/dashboard/manager/tasks/create.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in doGet: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }


    private List<Task> filterTasksFromRequest(HttpServletRequest request) {
        String[] tagIds = request.getParameterValues("tags[]");
        String startDateStr = request.getParameter("startDate");
        String dueDateStr = request.getParameter("dueDate");

        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty())
                ? LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE)
                : null;

        LocalDate dueDate = (dueDateStr != null && !dueDateStr.isEmpty())
                ? LocalDate.parse(dueDateStr, DateTimeFormatter.ISO_DATE)
                : null;

        return taskService.filterTasks(tagIds, startDate, dueDate);
    }


    private void setTaskStatistics(HttpServletRequest request, List<Task> tasks) {
        int tasksCount = tasks.size();
        int completedTasks = (int) tasks.stream().filter(task -> task.getStatus() == TaskStatus.DONE).count();
        int inProgressTasks = (int) tasks.stream().filter(task -> task.getStatus() == TaskStatus.IN_PROGRESS).count();
        int uncompletedTasks = tasksCount - (completedTasks + inProgressTasks);

        double completedPercentage = tasksCount > 0 ? (double) completedTasks / tasksCount * 100 : 0;
        double inProgressPercentage = tasksCount > 0 ? (double) inProgressTasks / tasksCount * 100 : 0;
        double uncompletedPercentage = tasksCount > 0 ? (double) uncompletedTasks / tasksCount * 100 : 0;

        request.setAttribute("tasksCount", tasksCount);
        request.setAttribute("completedPercentage", String.format("%.2f", completedPercentage));
        request.setAttribute("inProgressPercentage", String.format("%.2f", inProgressPercentage));
        request.setAttribute("uncompletedPercentage", String.format("%.2f", uncompletedPercentage));
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


            Optional<User> optionalCreator = userService.findUserById(creatorId);
            Optional<User> optionalAssignedUser = userService.findUserById(assignedUserId);

            optionalCreator.ifPresent(task::setCreator);
            optionalAssignedUser.ifPresent(task::setAssignedUser);


            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Optional<Tag> tag = tagService.findTagById(Long.parseLong(tagId));
                    tag.ifPresent(task.getTags()::add);
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
        Optional<List<Tag>> tagListOptional = tagService.findAllTags();
        List<Tag> tagList = tagListOptional.orElse(List.of());
        request.setAttribute("users", users);
        request.setAttribute("tagList", tagList);
    }
}
