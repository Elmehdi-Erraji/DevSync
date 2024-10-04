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

        if (user != null && PasswordUtils.hashPassword(password).equals(user.getPassword())) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            if (user.getRole() == Role.MANAGER) {
                response.sendRedirect("manager/users");
            } else if (user.getRole() == Role.USER) {
                response.sendRedirect("/views/dashboard/user/home.jsp");
            } else {
                response.sendRedirect("error.jsp?message=Unknown+role");
            }

        } else {
            response.sendRedirect("login.jsp?error=Invalid+email+or+password");
        }
    }
}

