package web.controller.manager;

import domain.Request;
import domain.Task;
import domain.User;
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

@WebServlet("/manager/request")
public class RequestServlet extends HttpServlet {

    private RequestService requestService;
    private TaskService taskService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            requestService = new RequestService();
            taskService = new TaskService(); // Ensure to initialize your TaskService
            userService = new UserService();   // Ensure to initialize your UserService
        } catch (Exception e) {
            throw new ServletException("Failed to initialize RequestServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Request> requestList = requestService.getAllRequests(); // Fetch all requests
        request.setAttribute("requests", requestList);
        request.getRequestDispatcher("/views/dashboard/manager/requests/home.jsp").forward(request, response);
    }

    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("requestId");

        // Check if the requestId parameter is present and not empty
        if (requestIdParam == null || requestIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required.");
            return; // Stop further processing
        }

        Long requestId;
        try {
            requestId = Long.parseLong(requestIdParam); // Safely parse the ID
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request ID format.");
            return; // Stop further processing
        }

        Optional<Request> requestToProcess = requestService.getRequestById(requestId); // Use Optional

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
               // processAcceptRequest(requestToProcess.get());
                break;
            case "DELETE":
                processDeleteRequest(requestToProcess); // Pass Optional<Request> directly
                break;
            case "REJECT":
                //processRejectRequest(requestToProcess.get());
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
        }

        response.sendRedirect("request"); // Redirect to the request list
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

        // Update tokens based on request type
        if (req.getRequestType() == RequestType.REJECT) {
            // Only update daily tokens
            Integer dailyTokens = user.getDailyTokens();
            if (dailyTokens != null) {
                userService.updateUserTokens((long) user.getId(), dailyTokens + 1, user.getMonthlyTokens());
            }
        } else if (req.getRequestType() == RequestType.DELETE) {
            // Only update monthly tokens
            Integer monthlyTokens = user.getMonthlyTokens();
            if (monthlyTokens != null) {
                userService.updateUserTokens((long) user.getId(), user.getDailyTokens(), monthlyTokens + 1);
            }
        }

        // Now delete the request from the system
        requestService.deleteRequest(req.getId());
    }
}
