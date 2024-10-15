<%@include file="../partials/sessionCheck.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.User" %>
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

                                        <form action="${pageContext.request.contextPath}/manager/request" method="POST" id="reassignTaskForm">
                                            <input type="hidden" name="requestId" value="<%= request.getAttribute("requestId") != null ? request.getAttribute("requestId") : "" %>"> <!-- Ensure this is included -->
                                            <input type="hidden" name="action" value="ACCEPT"> <!-- Action hidden input -->

                                            <div class="mb-3">
                                                <label for="title" class="form-label">Task Title</label>
                                                <input type="text" id="title" class="form-control" name="title" value="<%= request.getAttribute("taskTitle") != null ? request.getAttribute("taskTitle") : "" %>" readonly>
                                            </div>

                                            <div class="mb-3">
                                                <label for="description" class="form-label">Task Description</label>
                                                <textarea id="description" class="form-control" name="description" readonly><%= request.getAttribute("taskDescription") != null ? request.getAttribute("taskDescription") : "" %></textarea>
                                            </div>

                                            <input type="hidden" name="taskId" value="<%= request.getAttribute("taskId") != null ? request.getAttribute("taskId") : "" %>">

                                            <div class="mb-3">
                                                <label for="assignedUser" class="form-label">Reassign to User</label>
                                                <select class="form-select" id="assignedUser" name="newAssignedUser" required>
                                                    <%
                                                        List<User> userList = (List<User>) request.getAttribute("users");
                                                        User requestUser = (User) request.getAttribute("requestUser");

                                                        if (requestUser != null) {
                                                    %>
                                                    <option disabled>Request Created By: <%= requestUser.getUsername() %></option>
                                                    <%
                                                        }

                                                        // List the other users, excluding the request user
                                                        if (userList != null && !userList.isEmpty()) {
                                                            for (User user : userList) {
                                                                if (user.getRole() == Role.USER && !user.equals(requestUser)) { // Exclude the request user
                                                    %>
                                                    <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
                                                    <%
                                                            }
                                                        }
                                                    } else {
                                                    %>
                                                    <option disabled>No users available</option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <button type="submit" id="submitButton" class="btn btn-primary" name="reassignTask">Reassign Task</button>
                                            <a href="request" class="btn btn-secondary">Go Back</a>
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



