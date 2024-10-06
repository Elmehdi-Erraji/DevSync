package web.controller.manager;

import domain.Request;
import domain.Task;
import domain.User;
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

        response.sendRedirect("/manager/request"); // Redirect to the request list
    }


    private void processDeleteRequest(Optional<Request> request) {
        if (request.isPresent()) {
            Task task = request.get().getTask();
            User user = request.get().getUser();

            // Check if user is null
            if (user == null) {
                throw new NullPointerException("User associated with the request is null.");
            }

            // Ensure you have valid tokens before proceeding
            userService.updateUserTokens((long) user.getId(), user.getDailyTokens(), user.getMonthlyTokens() + 1); // Update monthly tokens
            taskService.deleteTask(task.getId()); // Delete the task

            requestService.deleteRequest(request.get().getId()); // Delete the request
        } else {
            throw new NullPointerException("Request is empty when trying to delete.");
        }


    }
}
