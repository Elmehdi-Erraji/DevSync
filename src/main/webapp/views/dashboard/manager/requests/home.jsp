<%@include file="../partials/sessionCheck.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Tag" %>
<%@ page import="domain.Request" %>
<%@ page import="domain.enums.RequestStatus" %>
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
                <!-- Start Page Title -->
                <div class="row">
                    <div class="col-12">
                        <div class="page-title-box">
                            <div class="page-title-right">
                                <ol class="breadcrumb m-0">
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Tags !</li>
                                </ol>
                            </div>
                            <h4 class="page-title">Welcome!</h4>
                        </div>
                    </div>
                </div>
                <!-- End Page Title -->

                <div class="row">
                    <div class="col-xxl-3 col-sm-6">
                        <div class="card widget-flat text-bg-primary">
                            <div class="card-body">
                                <div class="float-end">
                                    <i class="ri-group-2-line widget-icon"></i>
                                </div>
                                <h6 class="text-uppercase mt-0" title="Users">Requests</h6>
                                <h2 class="my-2">5</h2>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body p-0">
                                <div class="p-3">

                                </div>

                                <div id="requests-table-collapse" class="collapse show">
                                    <div class="table-responsive">
                                        <table class="table table-nowrap table-hover mb-0">
                                            <thead>
                                            <tr>
                                                <th>Request ID</th>
                                                <th>Task ID</th>
                                                <th>User ID</th>
                                                <th>Request Type</th>
                                                <th>Status</th>
                                                <th>Request Date</th>
                                                <th>Actions</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <%
                                                List<Request> reqList = (List<Request>) request.getAttribute("requests");
                                                if (reqList != null && !reqList.isEmpty()) {
                                                    for (Request req : reqList) {
                                            %>
                                            <tr>
                                                <td><%= req.getId() %></td>
                                                <td><%= req.getTask().getId() %></td>
                                                <td><%= req.getUser().getUsername()%></td>
                                                <td><%= req.getRequestType() %></td>
                                                <td>
                                                    <%
                                                        RequestStatus status = req.getStatus();
                                                        if (status == RequestStatus.PENDING) {
                                                    %>
                                                    <span class="badge bg-info-subtle text-info">Pending</span>
                                                    <%
                                                    } else if (status == RequestStatus.APPROVED) {
                                                    %>
                                                    <span class="badge bg-success-subtle text-success">Approved</span>
                                                    <%
                                                    } else if (status == RequestStatus.REJECTED) {
                                                    %>
                                                    <span class="badge bg-danger-subtle text-danger">Rejected</span>
                                                    <%
                                                    } else {
                                                    %>
                                                    <span class="badge bg-warning">Unknown Status</span>
                                                    <%
                                                        }
                                                    %>
                                                </td>
                                                <td><%= req.getRequestDate() %></td>
                                                <td>
                                                    <!-- Accept Button -->
                                                    <form action="${pageContext.request.contextPath}/manager/request" method="GET" class="d-inline">
                                                        <input type="hidden" name="requestId" value="<%= req.getId() %>"/>
                                                        <input type="hidden" name="action" value="ACCEPT"/> <!-- Specify the action -->
                                                        <button type="submit" class="btn btn-sm btn-success">Accept</button>
                                                    </form>

                                                    <!-- Delete Button -->
                                                    <form action="${pageContext.request.contextPath}/manager/request" method="POST" class="d-inline">
                                                        <input type="hidden" name="requestId" value="<%= req.getId() %>"/>
                                                        <input type="hidden" name="action" value="DELETE"> <!-- Delete type -->
                                                        <button type="submit" class="btn btn-sm btn-danger">Decline</button>
                                                    </form>
                                                </td>
                                            </tr>
                                            <%
                                                }
                                            } else {
                                            %>
                                            <tr>
                                                <td colspan="7" class="text-center">No requests available</td>
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



