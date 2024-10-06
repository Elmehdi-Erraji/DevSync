<%@ page import="javax.servlet.http.HttpSession" %>

<%
    HttpSession currentSession = request.getSession(false);


    if (currentSession == null || currentSession.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String userRole = (String) currentSession.getAttribute("role");
    if (!"USER".equals(userRole)) {
        currentSession.setAttribute("errorMessage", "You are not authorized to access that page.");
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
