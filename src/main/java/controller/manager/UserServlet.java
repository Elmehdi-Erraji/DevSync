package controller.manager;

import model.User;
import model.enums.Role;
import service.UserService;

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

    // Display all users
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
        String id = request.getParameter("id"); // For update operations

        // Handle delete request
        if ("delete".equalsIgnoreCase(method)) {
            if (id != null && !id.isEmpty()) {
                userService.deleteUser(Long.parseLong(id));
            }
        } else {
            // Common fields for both create and update
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String roleParam = request.getParameter("role"); // Get the role from the request

            // Debugging logs
            System.out.println("Username: " + username);
            System.out.println("Email: " + email);
            System.out.println("Role: " + roleParam); // Log the role for debugging

            // Check for null values
            if (username == null || email == null || roleParam == null) {
                throw new ServletException("Required form fields are missing");
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setDailyTokens(2); // Default tokens

            // Set the role in the User object
            try {
                Role role = Role.valueOf(roleParam.toUpperCase());
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                throw new ServletException("Invalid role specified");
            }

            // Handle create and update operations
            if (id != null && !id.isEmpty()) {
                // Update operation - do not change the password
                user.setId((int) Long.parseLong(id));
                // No need to set the password for update, just update the other fields
                userService.updateUser(user);
            } else {
                // Create operation
                String password = request.getParameter("password");
                if (password == null || password.isEmpty()) {
                    throw new ServletException("Password is required for creating a user");
                }

                // Hash the password using the hashPassword method
                String hashedPassword = hashPassword(password); // Use the hashing method
                user.setPassword(hashedPassword); // Store the hashed password

                userService.insertUser(user);
            }
        }

        response.sendRedirect("users?status=success"); // Redirect after processing
    }

    // Method to hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8)); // Use UTF-8 encoding
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    // Delete user
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        userService.deleteUser(id);
        response.sendRedirect("users");
    }
}
