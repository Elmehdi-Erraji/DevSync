<%@include file="../partials/sessionCheck.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.User" %>
<%@ page import="domain.Task" %>
<%@ page import="domain.Tag" %>
<%@ page import="domain.enums.TaskStatus" %>
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
                <div class="row">
                    <div class="col-xxl-3 col-sm-6">
                        <div class="card widget-flat text-bg-secondary" style="height: 100%;"> <!-- Changed color to 'secondary' -->
                            <div class="card-body">
                                <div class="float-end">
                                    <i class="ri-task-line widget-icon" style="font-size: 2rem;"></i> <!-- Task icon -->
                                </div>
                                <h6 class="text-uppercase mt-0" title="Tasks">Tasks</h6>
                                <h2 class="my-2"><%= request.getAttribute("tasksCount") %></h2> <!-- Display total tasks -->
                            </div>
                        </div>
                    </div>

                    <div class="col-xxl-3 col-sm-6"> <!-- Updated column size to match -->
                        <div class="card widget-flat text-bg-success" style="height: 100%;"> <!-- Success color for completed tasks -->
                            <div class="card-body">
                                <div class="float-end">
                                    <i class="ri-check-line widget-icon" style="font-size: 2rem;"></i> <!-- Check icon -->
                                </div>
                                <h6 class="text-uppercase mt-0" title="Completed Tasks">Completed</h6>
                                <h2 class="my-2"><%=request.getAttribute("completedPercentage")%> %</h2> <!-- Display completed percentage -->
                            </div>
                        </div>
                    </div>

                    <div class="col-xxl-3 col-sm-6"> <!-- Updated column size to match -->
                        <div class="card widget-flat text-bg-warning" style="height: 100%;"> <!-- Warning color for in-progress tasks -->
                            <div class="card-body">
                                <div class="float-end">
                                    <i class="ri-loader-4-line widget-icon" style="font-size: 2rem;"></i> <!-- Loader icon -->
                                </div>
                                <h6 class="text-uppercase mt-0" title="In Progress Tasks">In Progress</h6>
                                <h2 class="my-2"><%=request.getAttribute("inProgressPercentage")%> %</h2> <!-- Display in-progress percentage -->
                            </div>
                        </div>
                    </div>

                    <div class="col-xxl-3 col-sm-6"> <!-- Updated column size to match -->
                        <div class="card widget-flat text-bg-danger" style="height: 100%;"> <!-- Danger color for uncompleted tasks -->
                            <div class="card-body">
                                <div class="float-end">
                                    <i class="ri-error-warning-line widget-icon" style="font-size: 2rem;"></i> <!-- Error icon -->
                                </div>
                                <h6 class="text-uppercase mt-0" title="Uncompleted Tasks">Uncompleted</h6>
                                <h2 class="my-2"><%=request.getAttribute("uncompletedPercentage")%> %</h2> <!-- Display uncompleted percentage -->
                            </div>
                        </div>
                    </div>
                </div>

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
                                <!-- Tasks Table -->
                                <div id="tasks-table-collapse" class="collapse show">
                                    <div class="table-responsive">
                                        <table class="table table-nowrap table-hover mb-0">
                                            <thead>
                                            <tr>
                                                <th>Task ID</th>
                                                <th>Title</th>
                                                <th>Description</th>
                                                <th>Start Date</th>
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
                                                List<Task> taskList = (List<Task>) request.getAttribute("tasks");
                                                if (taskList != null && !taskList.isEmpty()) {
                                                    for (Task task : taskList) {
                                            %>
                                            <tr>
                                                <td><%= task.getId() %></td>
                                                <td><%= task.getTitle() %></td>
                                                <td><%= task.getDescription() %></td>
                                                <td><%=task.getStartDate()%></td>
                                                <td><%= task.getDueDate() %></td>
                                                <td>
                                                    <%
                                                        TaskStatus status = task.getStatus();
                                                        if (status == TaskStatus.NEW) {
                                                    %>
                                                    <span class="badge bg-info-subtle text-info">New</span>
                                                    <%
                                                    } else if (status == TaskStatus.IN_PROGRESS) {
                                                    %>
                                                    <span class="badge bg-warning-subtle text-warning">In Progress</span>
                                                    <%
                                                    } else if (status == TaskStatus.DONE) {
                                                    %>
                                                    <span class="badge bg-pink-subtle text-pink">Done</span>
                                                    <%
                                                    } else {
                                                    %>
                                                    <span class="badge bg-warning">Unknown Status</span>
                                                    <%
                                                        }
                                                    %>
                                                </td>

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
                                                <td colspan="9" class="text-center">No tasks available</td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <!-- End Tasks Table -->
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



