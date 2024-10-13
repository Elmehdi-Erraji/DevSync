package web.controller.manager;

import domain.Request;
import domain.Task;
import domain.User;
import domain.enums.RequestStatus;
import domain.enums.RequestType;
import service.RequestService;
import service.TaskService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/manager/request")
public class RequestServlet extends HttpServlet {

    private RequestService requestService;
    private TaskService taskService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            requestService = new RequestService();
            taskService = new TaskService();
            userService = new UserService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize RequestServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("ACCEPT")) {
            handleAcceptRedirect(request, response);
        } else {
            List<Request> requestList = requestService.getAllRequests();
            request.setAttribute("requests", requestList);
            request.getRequestDispatcher("/views/dashboard/manager/requests/home.jsp").forward(request, response);
        }
    }

    private void handleAcceptRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");

        Long requestId;
        try {
            requestId = Long.parseLong(requestIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request ID format.");
            return;
        }

        Optional<Request> requestToProcess = requestService.getRequestById(requestId);

        if (requestToProcess.isPresent()) {
            Request req = requestToProcess.get();
            Task task = req.getTask();

            request.setAttribute("requestId", requestId); // Set the request ID attribute

            if (task == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No task associated with the request.");
                return;
            }

            Long userRequestId = (long) req.getUser().getId();

            requestService.updateRequestStatus(requestId, RequestStatus.APPROVED);

            request.setAttribute("taskTitle", task.getTitle());
            request.setAttribute("taskDescription", task.getDescription());
            request.setAttribute("taskId", task.getId());
            request.setAttribute("requestUserId", req.getUser().getId());
            List<User> users = userService.findAllUsers();
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getId() != userRequestId)
                    .collect(Collectors.toList());

            request.setAttribute("users", filteredUsers);

            request.getRequestDispatcher("/views/dashboard/manager/requests/reassignTask.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");

        if (requestIdParam == null || requestIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required.");
            return;
        }

        Long requestId;
        try {
            requestId = Long.parseLong(requestIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request ID format.");
            return;
        }

        Optional<Request> requestToProcess = requestService.getRequestById(requestId);

        if (requestToProcess.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required.");
            return;
        }

        switch (action.toUpperCase()) {
            case "ACCEPT":
                Long taskId = Long.parseLong(request.getParameter("taskId"));
                Long assignedUserId = Long.parseLong(request.getParameter("newAssignedUser"));
                processAcceptRequest(taskId, assignedUserId);
                break;
            case "DELETE":
                processDeclineRequest(requestToProcess);
                break;
            case "REJECT":
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
        }

        response.sendRedirect("request");
    }

    private void processDeclineRequest(Optional<Request> request) {
        if (!request.isPresent()) {
            throw new IllegalArgumentException("Request is not present.");
        }

        Request req = request.get();
        Task task = req.getTask();
        User user = req.getUser();

        if (user == null) {
            throw new NullPointerException("User associated with the request is null.");
        }

        if (task == null) {
            throw new NullPointerException("Task associated with the request is null.");
        }

        if (req.getRequestType() == RequestType.REJECT) {
                userService.updateUserTokens((long) user.getId(), user.getDailyTokens() + 1, user.getMonthlyTokens());
        } else if (req.getRequestType() == RequestType.DELETE) {
                userService.updateUserTokens((long) user.getId(), user.getDailyTokens(), user.getMonthlyTokens() + 1);
        }
        requestService.updateRequestStatus(req.getId(), RequestStatus.REJECTED);
    }



    private void processAcceptRequest(Long taskId, Long assignedUserId) {
        System.out.println("test test test test ");
        Task task = taskService.findTaskById(taskId);

        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }
        User assignedUser = userService.findUserById(assignedUserId);

        if (assignedUser == null) {
            throw new IllegalArgumentException("Assigned user not found.");
        }

        task.setAssignedUser(assignedUser);
        task.setRefused(true);
        taskService.updateTask(task);
    }
}
