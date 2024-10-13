package web.controller.user;

import domain.Request;
import domain.Task;
import domain.User;
import domain.enums.RequestStatus;
import domain.enums.RequestType;
import service.RequestService;
import service.TagService;
import service.TaskService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/request")
public class RequestServlet extends HttpServlet {

    private RequestService requestService;
    private TaskService taskService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            requestService = new RequestService(); // Inject your request service
            taskService = new TaskService();       // Inject your task service
            userService = new UserService();       // Inject your user service
        } catch (Exception e) {
            throw new ServletException("Failed to initialize RequestServlet", e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long taskId = Long.parseLong(request.getParameter("taskId"));
        Long userId = Long.parseLong(request.getParameter("user_id"));
        String requestType = request.getParameter("requestType");

        Task task = taskService.findTaskById(taskId);
        User user = userService.findUserById(userId);

        if (task == null) {
            throw new ServletException("Task not found with id: " + taskId);
        }

        if (user == null) {
            throw new ServletException("User not found with id: " + userId);
        }

        Request requestToSave = new Request();
        requestToSave.setTask(task);
        requestToSave.setUser(user);

        if ("REJECT".equalsIgnoreCase(requestType)) {
            requestToSave.setRequestType(RequestType.REJECT);

            Integer dailyTokens = (Integer) request.getSession().getAttribute("dailyTokens");
            if (dailyTokens != null && dailyTokens > 0) {
                requestService.saveRequest(requestToSave);

                request.getSession().setAttribute("dailyTokens", dailyTokens - 1);
                userService.updateUserTokens(userId, dailyTokens - 1, user.getMonthlyTokens()); // Update in DB
            } else {
                // Handle insufficient tokens
                request.setAttribute("errorMessage", "Insufficient daily tokens to reject the request.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
        } else if ("DELETE".equalsIgnoreCase(requestType)) {
            requestToSave.setRequestType(RequestType.DELETE);

            Integer monthlyTokens = (Integer) request.getSession().getAttribute("monthlyTokens");
            if (monthlyTokens > 0) {
                requestService.saveRequest(requestToSave);

                request.getSession().setAttribute("monthlyTokens", monthlyTokens - 1);
                userService.updateUserTokens(userId, user.getDailyTokens(), monthlyTokens - 1); // Update in DB
            } else {
                request.setAttribute("errorMessage", "Insufficient monthly tokens to delete the request.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
        }

        response.sendRedirect("/demo2/user/tasks");
    }

}
