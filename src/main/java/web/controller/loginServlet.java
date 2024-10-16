package web.controller;

import domain.User;
import domain.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import service.UserService;
import util.PasswordUtils;
import web.vm.LoginVM;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@WebServlet("/")
public class loginServlet  extends HttpServlet {

    private UserService userService = new UserService();
    private Validator validator;

    @Override
    public void init() throws ServletException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LoginVM loginVM = new LoginVM();
        loginVM.setEmail(request.getParameter("email"));
        loginVM.setPassword(request.getParameter("password"));

        Set<ConstraintViolation<LoginVM>> violations = validator.validate(loginVM);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<LoginVM> violation : violations) {
                request.setAttribute("errorMessage", violation.getMessage());
                request.getRequestDispatcher("views/auth/login.jsp").forward(request, response);
                return;
            }
        }

        User user = authenticateUser(loginVM.getEmail(), loginVM.getPassword());
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("id", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole().toString());
            session.setAttribute("dailyTokens", user.getDailyTokens());
            session.setAttribute("monthlyTokens", user.getMonthlyTokens());

            if (user.getRole() == Role.MANAGER) {
                response.sendRedirect("manager/users");
            } else if (user.getRole() == Role.USER) {
                response.sendRedirect("user/tasks");
            } else {
                response.sendRedirect("error.jsp?message=Unknown+role");
            }
        } else {
            request.setAttribute("errorMessage", "Invalid email or password."); // Set error message
            request.getRequestDispatcher("views/auth/login.jsp").forward(request, response); // Forward to login page
        }
    }

    private User authenticateUser(String email, String password) {
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = PasswordUtils.hashPassword(password);
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        }

        return null;
    }
}
