package web.controller.manager;

import domain.User;
import domain.enums.Role;
import service.UserService;
import util.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@WebServlet("/manager/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize UserServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            List<User> userList = userService.findAllUsers();
            request.setAttribute("users", userList);
            request.getRequestDispatcher("/views/dashboard/manager/users/home.jsp").forward(request, response);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            User user = userService.findUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/views/dashboard/manager/users/edit.jsp").forward(request, response);
        } else if (action.equals("create")) {
            request.getRequestDispatcher("/views/dashboard/manager/users/create.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String id = request.getParameter("id");

        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                userService.deleteUser(Long.parseLong(id));
            }
        } else {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String roleParam = request.getParameter("role");

            if (username == null || email == null || roleParam == null) {
                throw new ServletException("Required form fields are missing");
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setDailyTokens(2);

            try {
                Role role = Role.valueOf(roleParam.toUpperCase());
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                throw new ServletException("Invalid role specified");
            }

            if (id != null && !id.isEmpty()) {
                user.setId((int) Long.parseLong(id));
                userService.updateUser(user);
            } else {
                String password = request.getParameter("password");
                if (password == null || password.isEmpty()) {
                    throw new ServletException("Password is required for creating a user");
                }

                String hashedPassword = PasswordUtils.hashPassword(password);
                user.setPassword(hashedPassword);

                userService.insertUser(user);
            }
        }

        response.sendRedirect("users?status=success");
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("users");
    }
}
