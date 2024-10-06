package web.controller;

import domain.User;
import domain.enums.Role;
import service.UserService;
import util.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class loginServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.findByEmail(email);

        if (user != null) {
            String hashedInputPassword = PasswordUtils.hashPassword(password);

            // Print for debugging
            System.out.println("Raw Password: " + password);
            System.out.println("Hashed Input Password: " + hashedInputPassword);
            System.out.println("Stored Password: " + user.getPassword());

            if (hashedInputPassword.equals(user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("id", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().toString());

                if (user.getRole() == Role.MANAGER) {
                    response.sendRedirect("manager/users");
                } else if (user.getRole() == Role.USER) {
                    response.sendRedirect("user/tasks");
                } else {
                    response.sendRedirect("error.jsp?message=Unknown+role");
                }
            } else {
                response.sendRedirect("login?error=Invalid+email+or+password");
            }
        } else {
            response.sendRedirect("login?error=Invalid+email+or+password");
        }
    }

}
