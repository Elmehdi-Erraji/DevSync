<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="domain.User" %>
<%
    // Check if we are editing an existing user
    User user = (User) request.getAttribute("user"); // Assuming you set the user object in request scope
%>
<html>


<head>
    <title><%= user != null ? "Update User" : "Create User" %></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4"><%= user != null ? "Update User" : "Create User" %></h1>
    <form action="users" method="post">
        <input type="hidden" name="id" value="<%= user != null ? user.getId() : "" %>"/> <!-- Include user ID for updates -->

        <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" id="name" name="name" value="<%= user != null ? user.getUsername() : "" %>" required/>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" value="<%= user != null ? user.getEmail() : "" %>" required/>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" value="<%= user != null ? user.getPassword() : "" %>" required/>
        </div>

        <button type="submit" class="btn btn-primary">
            <%= user != null ? "Update" : "Create" %> <!-- Change button text based on context -->
        </button>

        <a href="users" class="btn btn-secondary">Cancel</a>
        <a href="home" class="btn btn-info">Back to Home</a> <!-- Add Back to Home button -->
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
