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

            // Ensure you set the requestId attribute here
            request.setAttribute("requestId", requestId); // Set the request ID attribute

            if (task == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No task associated with the request.");
                return;
            }

            Long userRequestId = (long) req.getUser().getId();

            // Update the request status to approved
            requestService.updateRequestStatus(requestId, RequestStatus.APPROVED); // Update the request status to APPROVED

            request.setAttribute("taskTitle", task.getTitle());
            request.setAttribute("taskDescription", task.getDescription());
            request.setAttribute("taskId", task.getId());
            request.setAttribute("requestUserId", req.getUser().getId());
            List<User> users = userService.findAllUsers();
            List<User> filteredUsers = users.stream()
                    .filter(user -> user.getId() != userRequestId) // Use primitive comparison
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
        System.out.println("Received requestIdParam: " + requestIdParam); // Debugging line

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
                Long assignedUserId = Long.parseLong(request.getParameter("newAssignedUser")); // Ensure this matches your form input name
                processAcceptRequest(taskId, assignedUserId);
                break;
            case "DELETE":
                processDeleteRequest(requestToProcess);
                break;
            case "REJECT":
                // Handle reject action (implementation can be added later)
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
        }

        // Redirect back to the request list
        response.sendRedirect("request");
    }

    private void processDeleteRequest(Optional<Request> request) {
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

        // Check if it's a REJECT type of request and handle doubling daily tokens
        if (req.getRequestType() == RequestType.REJECT) {
            Integer dailyTokens = user.getDailyTokens();
            if (dailyTokens != null) {
                // Double the user's daily tokens
                userService.updateUserTokens((long) user.getId(), dailyTokens * 2, user.getMonthlyTokens());
            }
        }
        // Check if it's a DELETE type of request and handle doubling monthly tokens
        else if (req.getRequestType() == RequestType.DELETE) {
            Integer monthlyTokens = user.getMonthlyTokens();
            if (monthlyTokens != null) {
                // Double the user's monthly tokens
                userService.updateUserTokens((long) user.getId(), user.getDailyTokens(), monthlyTokens * 2);
            }
        }

        // Change the request status to REJECTED instead of deleting the request
        requestService.updateRequestStatus(req.getId(), RequestStatus.REJECTED); // Pass ID and status separately
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

        // Update the task with new assigned user and mark it as refused
        task.setAssignedUser(assignedUser);
        task.setRefused(true); // Change the status to refused
        taskService.updateTask(task); // Save changes to the database
    }
}
