<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    // Check if we are editing an existing user
    User user = (User) request.getAttribute("user"); // Assuming you set the user object in request scope
%>
<html>
<head>
    <title><%= user != null ? "Update User" : "Create User" %></title>
</head>
<body>
<h1><%= user != null ? "Update User" : "Create User" %></h1>
<form action="users" method="post">
    <input type="hidden" name="id" value="<%= user != null ? user.getId() : "" %>"/> <!-- Include user ID for updates -->
    <label>Name:</label>
    <input type="text" name="name" value="<%= user != null ? user.getUsername() : "" %>" required/>
    <br/>
    <label>Email:</label>
    <input type="email" name="email" value="<%= user != null ? user.getEmail() : "" %>" required/>
    <br/>
    <label>Password:</label>
    <input type="password" name="password" value="<%= user != null ? user.getPassword() : "" %>" required/>
    <br/>
    <input type="submit" value="<%= user != null ? "Update" : "Create" %>"/> <!-- Change button text based on context -->
</form>
</body>
</html>
