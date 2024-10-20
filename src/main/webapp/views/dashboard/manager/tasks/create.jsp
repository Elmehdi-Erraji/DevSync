<%@include file="../partials/sessionCheck.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Task" %>
<%@ page import="domain.User" %>
<%@ page import="domain.Tag" %>
<%@ page import="domain.enums.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>




<!DOCTYPE html>
<html lang="en">
<%@ include file="../partials/head.jsp" %>
<body>
<div class="wrapper">


    <!-- ========== Top bar start ========== -->
    <%@ include file="../partials/topbar.jsp" %>
    <!-- ========== Top bar End ========== -->

    <!-- ========== Left Sidebar Start ========== -->
    <%@ include file="../partials/sidebar.jsp" %>
    <!-- ========== Left Sidebar End ========== -->



    <!-- ============================================================== -->
    <!-- Start Page Content here -->
    <!-- ============================================================== -->

    <div class="content-page">
        <div class="content">
            <!-- Start Content-->
            <div class="container-fluid">
                <!-- start page title -->
                <div class="row">
                    <div class="col-12">
                        <div class="page-title-box">
                            <div class="page-title-right">
                                <ol class="breadcrumb m-0">
                                    <li class="breadcrumb-item"><a href="javascript:void(0);">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Tasks !</li>
                                </ol>
                            </div>
                            <h4 class="page-title">Create a task here</h4>
                        </div>
                    </div>
                </div>
                <!-- end page title -->

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="header-title">Add a new task</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-lg-6">

                                        <form action="${pageContext.request.contextPath}/manager/tasks" method="POST" id="addTaskForm">

                                            <%
                                                String errorMessage = (String) request.getAttribute("error");
                                                if (errorMessage != null) {
                                            %>
                                            <div class="alert alert-danger" role="alert">
                                                <%= errorMessage %>
                                            </div>
                                            <%
                                                }
                                            %>

                                            <div class="mb-3">
                                                <label for="title" class="form-label">Task Title</label>
                                                <input type="text" id="title" class="form-control" name="title" placeholder="Enter task title" required>
                                            </div>

                                            <div class="mb-3">
                                                <label for="description" class="form-label">Task Description</label>
                                                <textarea id="description" class="form-control" name="description" placeholder="Enter task description" required></textarea>
                                            </div>
                                            <div class="mb-3">
                                                <label for="startDate" class="form-label">Start Date</label>
                                                <input type="date" id="startDate" class="form-control" name="startDate" min="<%= java.time.LocalDate.now().plusDays(3) %>" required>
                                            </div>


                                            <div class="mb-3">
                                                <label for="dueDate" class="form-label">Due Date</label>
                                                <input type="date" id="dueDate" class="form-control" name="dueDate" min="<%= java.time.LocalDate.now().plusDays(4) %>"required>
                                            </div>

                                            <input type="hidden" name="creator" value="<%= session.getAttribute("id") %>">

                                            <div class="mb-3">
                                                <label for="assignedUser" class="form-label">Assigned User</label>
                                                <select class="form-select" id="assignedUser" name="assignedUser" required>
                                                    <%
                                                        List<User> userList = (List<User>) request.getAttribute("users");

                                                        if (userList != null && !userList.isEmpty()) {
                                                            for (User user : userList) {
                                                                if (user.getRole() == Role.USER) {
                                                    %>
                                                    <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
                                                    <%
                                                                }
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>


                                            <div class="mb-3">
                                                <label for="tags" class="form-label">Tags</label>
                                                <select multiple class="form-select" id="tags" name="tags">
                                                    <%
                                                        List<Tag> tagList = (List<Tag>) request.getAttribute("tagList");
                                                        if (tagList != null && !tagList.isEmpty()) {
                                                            for (Tag tag : tagList) {
                                                    %>
                                                    <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <button type="submit" id="submitButton" class="btn btn-primary" name="addTask">Submit</button>
                                            <a href="tasks" class="btn btn-secondary">Go Back</a>
                                        </form>


                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>



            <!-- End Content-->
        </div>



        <!-- Footer Start -->
        <%@ include file="../partials/footer.jsp" %>
        <!-- end Footer -->

    </div>


</div>
<!-- END wrapper -->
<!-- Vendor js -->
<script src="${pageContext.request.contextPath}/js/vendor.min.js"></script>

<!-- App js -->
<script src="${pageContext.request.contextPath}/js/app.min.js"></script>

</body>
</html>



