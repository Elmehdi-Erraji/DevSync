
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.User" %>
<%@ page import="domain.Task" %>
<%@ page import="domain.Tag" %>
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
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Mehdi</a></li>
                                    <li class="breadcrumb-item active">Admin Panel</li>
                                </ol>
                            </div>
                            <h4 class="page-title">Tasks List</h4>
                        </div>
                    </div>
                </div>
                <!-- end page title -->

                <!-- Display Tasks Table -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body p-0">
                                <div class="p-3">
                                    <div class="row">
                                        <div class="col-lg-6">
                                            <a href="tasks?action=create" class="btn btn-primary" id="addButton">Add A Task</a>
                                        </div>
                                    </div>
                                </div>

                                <!-- Tasks Assigned to User Table -->
                                <div id="tasks-table-collapse" class="collapse show">
                                    <div class="row">
                                        <div class="col-12">
                                            <h4 class="text-center mt-4">Tasks Assigned to You</h4> <!-- Centered Heading for Assigned Tasks -->
                                            <hr style="border-top: 2px solid black; width: 50%; margin: 10px auto;" /> <!-- Black line for separation -->
                                            <div class="table-responsive">
                                                <table class="table table-nowrap table-hover mb-0">
                                                    <thead>
                                                    <tr>
                                                        <th>Task ID</th>
                                                        <th>Title</th>
                                                        <th>Description</th>
                                                        <th>Due Date</th>
                                                        <th>Status</th>
                                                        <th>Creator</th>
                                                        <th>Assigned User</th>
                                                        <th>Tags</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <%
                                                        List<Task> assignedTaskList = (List<Task>) request.getAttribute("assignedTasks");
                                                        if (assignedTaskList != null && !assignedTaskList.isEmpty()) {
                                                            for (Task task : assignedTaskList) {
                                                    %>
                                                    <tr>
                                                        <td><%= task.getId() %></td>
                                                        <td><%= task.getTitle() %></td>
                                                        <td><%= task.getDescription() %></td>
                                                        <td><%= task.getDueDate() %></td>
                                                        <td><%= task.getStatus() %></td>
                                                        <td><%= task.getCreator().getUsername() %></td>
                                                        <td><%= task.getAssignedUser().getUsername() %></td>
                                                        <td>
                                                            <%
                                                                if (task.getTags() != null && !task.getTags().isEmpty()) {
                                                                    for (Tag tag : task.getTags()) {
                                                            %>
                                                            <span class="badge bg-info"><%= tag.getName() %></span>
                                                            <%
                                                                }
                                                            } else {
                                                            %>
                                                            <span>No Tags</span>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td>
                                                            <!-- Update Button -->
                                                            <a href="tasks?action=edit&id=<%= task.getId() %>" class="btn btn-sm btn-primary">Edit</a>

                                                            <!-- Delete Button with form for POST method -->
                                                            <form action="tasks" method="POST" class="d-inline">
                                                                <input type="hidden" name="id" value="<%= task.getId() %>"/>
                                                                <input type="hidden" name="_method" value="delete"/>
                                                                <button type="submit" class="btn btn-sm btn-danger delete-btn">Delete</button>
                                                            </form>
                                                        </td>
                                                    </tr>
                                                    <%
                                                        }
                                                    } else {
                                                    %>
                                                    <tr>
                                                        <td colspan="9" class="text-center">No tasks assigned to you</td>
                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- End Assigned Tasks Table -->

                                <!-- Separator for visual clarity -->
                                <hr class="my-4" /> <!-- Horizontal line for separation -->

                                <!-- Tasks Created by User Table -->
                                <div id="created-tasks-table-collapse" class="collapse show">
                                    <div class="row">
                                        <div class="col-12">
                                            <h4 class="text-center mt-4">Tasks Created by You</h4> <!-- Centered Heading for Created Tasks -->
                                            <hr style="border-top: 2px solid black; width: 50%; margin: 10px auto;" /> <!-- Black line for separation -->
                                            <div class="table-responsive">
                                                <table class="table table-nowrap table-hover mb-0">
                                                    <thead>
                                                    <tr>
                                                        <th>Task ID</th>
                                                        <th>Title</th>
                                                        <th>Description</th>
                                                        <th>Due Date</th>
                                                        <th>Status</th>
                                                        <th>Creator</th>
                                                        <th>Assigned User</th>
                                                        <th>Tags</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <%
                                                        List<Task> createdTaskList = (List<Task>) request.getAttribute("selfCreatedTasks");
                                                        if (createdTaskList != null && !createdTaskList.isEmpty()) {
                                                            for (Task task : createdTaskList) {
                                                    %>
                                                    <tr>
                                                        <td><%= task.getId() %></td>
                                                        <td><%= task.getTitle() %></td>
                                                        <td><%= task.getDescription() %></td>
                                                        <td><%= task.getDueDate() %></td>
                                                        <td><%= task.getStatus() %></td>
                                                        <td><%= task.getCreator().getUsername() %></td>
                                                        <td><%= task.getAssignedUser().getUsername() %></td>
                                                        <td>
                                                            <%
                                                                if (task.getTags() != null && !task.getTags().isEmpty()) {
                                                                    for (Tag tag : task.getTags()) {
                                                            %>
                                                            <span class="badge bg-info"><%= tag.getName() %></span>
                                                            <%
                                                                }
                                                            } else {
                                                            %>
                                                            <span>No Tags</span>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td>
                                                            <!-- Update Button -->
                                                            <a href="tasks?action=edit&id=<%= task.getId() %>" class="btn btn-sm btn-primary">Edit</a>

                                                            <!-- Delete Button with form for POST method -->
                                                            <form action="tasks" method="POST" class="d-inline">
                                                                <input type="hidden" name="id" value="<%= task.getId() %>"/>
                                                                <input type="hidden" name="_method" value="delete"/>
                                                                <button type="submit" class="btn btn-sm btn-danger delete-btn">Delete</button>
                                                            </form>
                                                        </td>
                                                    </tr>
                                                    <%
                                                        }
                                                    } else {
                                                    %>
                                                    <tr>
                                                        <td colspan="9" class="text-center">No tasks created by you</td>
                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- End Created Tasks Table -->

                            </div>
                        </div>
                    </div>
                </div>


            </div> <!-- container -->

        </div> <!-- content -->

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



