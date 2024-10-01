package controller;

import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService(); // Make sure this doesn't throw an exception
        } catch (Exception e) {
            throw new ServletException("Failed to initialize UserServlet", e);
        }
    }

    // Display all users
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            List<User> userList = userService.findAllUsers();
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("views/userList.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            User user = userService.findUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("views/userForm.jsp").forward(request, response);
        }
    }

    // Create and update user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        String id = request.getParameter("id");// Redirect after creation or update
        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                userService.deleteUser(Long.parseLong(id));
            }
        } else {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = new User(name, email, password);
            if (id != null && !id.isEmpty()) {
                user.setId(Long.parseLong(id));
                userService.updateUser(user);
            } else {
                userService.insertUser(user);
            }
        }
        response.sendRedirect("users?status=success"); // Redirect after deletion
    }

    // Delete user
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("users");
    }
}
