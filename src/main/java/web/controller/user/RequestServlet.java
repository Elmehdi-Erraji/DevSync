package web.controller.user;

import domain.Request;
import domain.Task;
import domain.TokenLog;
import domain.User;
import domain.enums.RequestStatus;
import domain.enums.RequestType;
import repository.UserRepository;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

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
            userService = new UserService(new UserRepository());
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
        if (task == null) {
            throw new ServletException("Task not found with ID: " + taskId);
        }

        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new ServletException("User not found with ID: " + userId);
        }
        User user = userOptional.get();

        // Check if a request already exists for this task
        Optional<Request> existingRequest = requestService.findRequestByTaskAndUser(taskId, userId);
        if (existingRequest.isPresent()) {
            request.setAttribute("errorMessage", "A request has already been made for this task. Please wait for the current request to be processed.");
            request.getRequestDispatcher("/user/tasks").forward(request, response);
            return;
        }

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

                tokenLog.setPreviousAssignedUser(task.getAssignedUser() != null ? task.getAssignedUser().getUsername() : null);
                tokenLog.setNewAssignedUser("");
                tokenLog.setManagerApproved(null);

                request.getSession().setAttribute("dailyTokens", dailyTokens - 1);
                userService.updateUserTokens(userId, dailyTokens - 1, user.getMonthlyTokens());
                tokenLogService.saveTokenLog(tokenLog);
            } else {
                request.setAttribute("errorMessage", "Insufficient daily tokens to reject the task.");
                request.getRequestDispatcher("/user/tasks").forward(request, response);
                return;
            }
        } else if ("DELETE".equalsIgnoreCase(requestType)) {
            Integer monthlyTokens = (Integer) request.getSession().getAttribute("monthlyTokens");

            if (monthlyTokens != null && monthlyTokens > 0) {
                tokenLog.setPreviousAssignedUser(null);
                tokenLog.setNewAssignedUser(null);
                tokenLog.setManagerApproved(null);

                request.getSession().setAttribute("monthlyTokens", monthlyTokens - 1);
                userService.updateUserTokens(userId, user.getDailyTokens(), monthlyTokens - 1);
                tokenLogService.saveTokenLog(tokenLog);
                taskService.deleteTask(taskId);
            } else {
                request.setAttribute("errorMessage", "Insufficient monthly tokens to delete the task.");
                request.getRequestDispatcher("/user/tasks").forward(request, response);
                return;
            }
        }

        response.sendRedirect("/user/tasks");
    }

}
