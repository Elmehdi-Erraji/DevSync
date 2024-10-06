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

@WebServlet("/manager/request")
public class RequestServlet extends HttpServlet {

    private RequestService requestService;

    @Override
    public void init() throws ServletException {
        try {
            requestService = new RequestService();

        } catch (Exception e) {
            throw new ServletException("Failed to initialize RequestServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Request> requestList = requestService.getAllRequests(); // Assuming you have this method implemented
        request.setAttribute("requests", requestList);
        request.getRequestDispatcher("/views/dashboard/manager/requests/home.jsp").forward(request, response);
    }

}
