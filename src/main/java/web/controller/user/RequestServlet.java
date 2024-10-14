package web.controller.user;

import domain.Request;
import domain.Task;
import domain.TokenLog;
import domain.User;
import domain.enums.RequestStatus;
import domain.enums.RequestType;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/request")
public class RequestServlet extends HttpServlet {

    private RequestService requestService;
    private TaskService taskService;
    private UserService userService;
    private TokenLogService tokenLogService;

    @Override
    public void init() throws ServletException {
        try {
            requestService = new RequestService();
            taskService = new TaskService();
            userService = new UserService();
            tokenLogService = new TokenLogService();
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

        // Initialize TokenLog
        TokenLog tokenLog = new TokenLog();
        tokenLog.setTokensUsed(1);
        tokenLog.setAction(requestType);
        tokenLog.setUsername(user.getUsername());
        tokenLog.setTaskId(taskId);
        tokenLog.setDateUsed(LocalDateTime.now());

        if ("REJECT".equalsIgnoreCase(requestType)) {
            Integer dailyTokens = (Integer) request.getSession().getAttribute("dailyTokens");

            if (dailyTokens != null && dailyTokens > 0) {
                Request rejectRequest = new Request();
                rejectRequest.setTask(task);
                rejectRequest.setUser(user);
                rejectRequest.setRequestType(RequestType.REJECT);
                requestService.saveRequest(rejectRequest);

                tokenLog.setPreviousAssignedUser(task.getAssignedUser().getUsername());
                tokenLog.setNewAssignedUser("");
                tokenLog.setManagerApproved(null);


                request.getSession().setAttribute("dailyTokens", dailyTokens - 1);
                userService.updateUserTokens(userId, dailyTokens - 1, user.getMonthlyTokens());
                tokenLogService.saveTokenLog(tokenLog);
            } else {
                request.setAttribute("errorMessage", "Insufficient daily tokens to reject the task.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
        } else if ("DELETE".equalsIgnoreCase(requestType)) {
            Integer monthlyTokens = (Integer) request.getSession().getAttribute("monthlyTokens");

            if (monthlyTokens != null && monthlyTokens > 0) {
                // Log the token usage for delete
                tokenLog.setPreviousAssignedUser(null); // Not relevant for delete
                tokenLog.setNewAssignedUser(null); // Not relevant for delete
                tokenLog.setManagerApproved(null); // No manager approval needed for delete

                // Update tokens and delete the task
                request.getSession().setAttribute("monthlyTokens", monthlyTokens - 1);
                userService.updateUserTokens(userId, user.getDailyTokens(), monthlyTokens - 1);
                tokenLogService.saveTokenLog(tokenLog); // Save the log
                taskService.deleteTask(taskId); // Delete the task
            } else {
                // Insufficient tokens handling
                request.setAttribute("errorMessage", "Insufficient monthly tokens to delete the task.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
        }

        // Redirect to the tasks page after the operation
        response.sendRedirect("/demo2/user/tasks");
    }

}
