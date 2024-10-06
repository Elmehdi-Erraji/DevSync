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
        String requestType = request.getParameter("requestType"); // Get request type

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
        } else if ("DELETE".equalsIgnoreCase(requestType)) {
            requestToSave.setRequestType(RequestType.DELETE);
        }

        requestService.saveRequest(requestToSave);

        response.sendRedirect("successPage.jsp");
    }

}
