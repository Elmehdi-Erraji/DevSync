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

@WebServlet("/request")
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long taskId = Long.parseLong(request.getParameter("taskId"));
        Long userId = Long.parseLong(request.getParameter("user_id"));
        String requestType = request.getParameter("requestType"); // Get request type

        // Retrieve task and user
        Task task = taskService.findTaskById(taskId);
        User user = userService.findUserById(userId);

        // Check if task is null
        if (task == null) {
            throw new ServletException("Task not found with id: " + taskId);
        }

        // Check if user is null
        if (user == null) {
            throw new ServletException("User not found with id: " + userId);
        }

        // Create a new request based on the requestType
        Request requestToSave = new Request();
        requestToSave.setTask(task);
        requestToSave.setUser(user);

        // Set the request type based on the value received
        if ("REJECT".equalsIgnoreCase(requestType)) {
            requestToSave.setRequestType(RequestType.REJECT);
        } else if ("DELETE".equalsIgnoreCase(requestType)) {
            requestToSave.setRequestType(RequestType.DELETE);
        }

        requestService.saveRequest(requestToSave);

        // Redirect or forward to a success page
        response.sendRedirect("successPage.jsp");
    }

}
