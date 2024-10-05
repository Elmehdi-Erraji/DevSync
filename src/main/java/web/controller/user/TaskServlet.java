package web.controller.user;

import domain.Tag;
import domain.Task;
import domain.User;
import domain.enums.TaskStatus;
import service.TagService;
import service.TaskService;
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

    // Display all tasks
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Ensure that the session and the logged-in user exist
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // Redirect to login if no user session
            return;
        }

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) ((HttpSession) session).getAttribute("user");

        // Check for the "action" parameter
        String action = request.getParameter("action");

        if (action == null) {
            List<Task> taskList = taskService.findTasksByUserId((long) loggedInUser.getId());
            request.setAttribute("tasks", taskList);
            request.getRequestDispatcher("/views/dashboard/user/tasks/home.jsp").forward(request, response);
        }
    }


}
