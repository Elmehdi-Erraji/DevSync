package web.controller.manager;

import domain.User;
import domain.enums.Role;
import repository.UserRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/manager/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService(new UserRepository());
        } catch (Exception e) {
            throw new ServletException("Failed to initialize UserServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            Optional<List<User>> userListOptional = Optional.ofNullable(userService.findAllUsers());

            List<User> userList = userListOptional.orElse(List.of());

            List<User> filteredUsers = userList.stream()
                    .filter(user -> user.getRole() == Role.USER)
                    .collect(Collectors.toList());

            int totalUsers = filteredUsers.size();

            request.setAttribute("filteredUsers", filteredUsers);
            request.setAttribute("totalUsers", totalUsers);

            request.getRequestDispatcher("/views/dashboard/manager/users/home.jsp").forward(request, response);
        }
        else if (action.equals("edit")) {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<User> userOptional = userService.findUserById(id);

            if (userOptional.isPresent()) {
                request.setAttribute("user", userOptional.get());
                request.getRequestDispatcher("/views/dashboard/manager/users/edit.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "User not found.");
                request.getRequestDispatcher("/views/dashboard/manager/users/error.jsp").forward(request, response);
            }
        }
        else if (action.equals("create")) {
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

            // Print all input values to debug
            System.out.println("Received POST data:");
            System.out.println("ID: " + id);
            System.out.println("Username: " + username);
            System.out.println("Email: " + email);
            System.out.println("Role: " + roleParam);

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

                // Print user object before calling update
                System.out.println("Updating user: " + user);

                userService.updateUser(user);
            } else {
                String password = request.getParameter("password");
                if (password == null || password.isEmpty()) {
                    throw new ServletException("Password is required for creating a user");
                }

                String hashedPassword = PasswordUtils.hashPassword(password);
                user.setPassword(hashedPassword);

                // Print user object before insertion
                System.out.println("Inserting new user: " + user);

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
