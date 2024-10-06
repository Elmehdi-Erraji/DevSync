package web.controller.user;

import domain.Request;
import domain.Task;
import domain.User;
import domain.enums.RequestStatus;
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
        try {
            // Get the taskId and userId from the form
            String taskIdStr = request.getParameter("taskId");
            String userIdStr = request.getParameter("user_id");

            if (taskIdStr == null || userIdStr == null) {
                throw new ServletException("Invalid form submission, missing taskId or userId");
            }

            // Parse taskId and userId
            Long taskId = Long.parseLong(taskIdStr);
            Long userId = Long.parseLong(userIdStr);

            // Fetch the corresponding Task and User entities
            Task task = taskService.findTaskById(taskId);
            User user = userService.findUserById(userId);

            if (task == null) {
                throw new ServletException("Task not found with id: " + taskId);
            }

            if (user == null) {
                throw new ServletException("User not found with id: " + userId);
            }

            // Create a new Request object
            Request requestEntity = new Request();
            requestEntity.setTask(task);           // Set the task
            requestEntity.setUser(user);           // Set the user
            requestEntity.setStatus(RequestStatus.REJECTED); // Set the status to REJECTED (assuming you want to reject the task here)
            requestEntity.setRequestDate(java.time.LocalDateTime.now()); // Set the request date

            // Save the request
            requestService.saveRequest(requestEntity);

            // Redirect back to a relevant page (you can change this based on your flow)
            response.sendRedirect("tasks?status=success");

        } catch (Exception e) {
            throw new ServletException("Failed to process request", e);
        }
    }
}
