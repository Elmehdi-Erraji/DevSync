
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Task" %>
<%@ page import="domain.User" %>
<%@ page import="domain.Tag" %>
<%@ page import="domain.enums.Role" %>
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

                                        <form action="${pageContext.request.contextPath}/user/tasks" method="POST" id="updateTaskForm">
                                            <%
                                                Task task = (Task) request.getAttribute("task");
                                            %>
                                            <input type="hidden" name="id" value="<%= task.getId() %>"/>
                                            <input type="hidden" name="_method" value="statusUpdate"/> <!-- Use PUT for updates -->

                                            <div class="mb-3">
                                                <label for="" class="form-label">Task Status</label>
                                                <select id="" class="form-select" name="status" required>
                                                    <%
                                                        for (TaskStatus status : TaskStatus.values()) {
                                                            boolean selected = task.getStatus() == status;
                                                    %>
                                                    <option value="<%= status.name() %>" <%= selected ? "selected" : "" %>><%= status.name() %></option>
                                                    <%
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



