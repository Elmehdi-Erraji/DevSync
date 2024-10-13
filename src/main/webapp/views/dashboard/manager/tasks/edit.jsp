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
                                    <li class="breadcrumb-item"><a href="javascript:void(0);"> </a></li>
                                    <li class="breadcrumb-item"><a href="javascript:void(0);">Dashboards</a></li>
                                    <li class="breadcrumb-item active">Welcome!</li>
                                </ol>
                            </div>
                            <h4 class="page-title">Update a task here</h4>
                        </div>
                    </div>
                </div>
                <!-- end page title -->

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="header-title">Task Update</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-lg-6">

                                        <form action="${pageContext.request.contextPath}/manager/tasks" method="POST" id="updateTaskForm">
                                            <%
                                                Task task = (Task) request.getAttribute("task");
                                            %>
                                            <input type="hidden" name="id" value="<%= task.getId() %>"/> <!-- Hidden input for task ID -->
                                            <input type="hidden" name="_method" value="PUT"/> <!-- Specify the update operation -->

                                            <div class="mb-3">
                                                <label for="title" class="form-label">Task Title</label>
                                                <input type="text" id="title" class="form-control" name="title" value="<%= task.getTitle() %>" required>
                                            </div>

                                            <div class="mb-3">
                                                <label for="description" class="form-label">Task Description</label>
                                                <textarea id="description" class="form-control" name="description" required><%= task.getDescription() %></textarea>
                                            </div>

                                            <div class="mb-3">
                                                <label for="startDate" class="form-label">Start Date</label>
                                                <input type="date" id="startDate" class="form-control" name="startDate" value="<%= task.getStartDate() %>" required>
                                            </div>

                                            <div class="mb-3">
                                                <label for="dueDate" class="form-label">Due Date</label>
                                                <input type="date" id="dueDate" class="form-control" name="dueDate" value="<%= task.getDueDate() %>" required>
                                            </div>

                                            <div class="mb-3">
                                                <label for="creator" class="form-label">Creator</label>
                                                <select class="form-select" id="creator" name="creator" required>
                                                    <%
                                                        List<User> userList = (List<User>) request.getAttribute("users");
                                                        if (userList != null && !userList.isEmpty()) {
                                                            for (User user : userList) {
                                                                if (user.getRole() == Role.USER) { // Check for USER role
                                                    %>
                                                    <option value="<%= user.getId() %>" <%= task.getCreator().getId() == user.getId() ? "selected" : "" %>><%= user.getUsername() %></option>
                                                    <%
                                                                }
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <div class="mb-3">
                                                <label for="assignedUser" class="form-label">Assigned User</label>
                                                <select class="form-select" id="assignedUser" name="assignedUser" required>
                                                    <%
                                                        if (userList != null && !userList.isEmpty()) {
                                                            for (User user : userList) {
                                                    %>
                                                    <option value="<%= user.getId() %>" <%= task.getAssignedUser().getId() == user.getId() ? "selected" : "" %>><%= user.getUsername() %></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <div class="mb-3">
                                                <label for="tags" class="form-label">Tags</label>
                                                <select multiple class="form-select" id="tags" name="tags">
                                                    <%
                                                        List<Tag> tagList = (List<Tag>) request.getAttribute("tags");
                                                        if (tagList != null && !tagList.isEmpty()) {
                                                            for (Tag tag : tagList) {
                                                                boolean selected = task.getTags().stream().anyMatch(t -> t.getId() == tag.getId());
                                                    %>
                                                    <option value="<%= tag.getId() %>" <%= selected ? "selected" : "" %>><%= tag.getName() %></option>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </select>
                                            </div>

                                            <button type="submit" id="submitButton" class="btn btn-primary" name="updateTask">Update Task</button>
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



