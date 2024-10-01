package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/demo2/*") // This will handle all requests starting with /app/
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo(); // Get the action from the URL

        switch (action) {
            case "/users":
                request.getRequestDispatcher("/users").forward(request, response); // Forward to UserServlet
                break;
            case "/about":
                request.getRequestDispatcher("/about").forward(request, response); // Forward to AboutServlet
                break;
            case "/home":
                request.getRequestDispatcher("/home").forward(request, response); // Forward to HomeServlet
                break;
            case "/login":
                request.getRequestDispatcher("/login").forward(request, response);
                break;

                case "/register":
                request.getRequestDispatcher("/register").forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
}
