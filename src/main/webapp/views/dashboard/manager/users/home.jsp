<%@include file="../partials/sessionCheck.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.User" %>
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
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Home</a></li>
                                    <li class="breadcrumb-item"><a href="javascript: void(0);">Dashboards</a></li>
                                    <li class="breadcrumb-item active">Users !</li>
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
                                <h6 class="text-uppercase mt-0" title="Users">Users</h6>
                                <h2 class="my-2"><%= request.getAttribute("totalUsers")%></h2>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body p-0">
                                <div class="p-3">
                                    <div class="row">
                                        <div class="col-lg-6">
                                            <a href="users?action=create" class="btn btn-primary" id="addButton">Add A User</a>
                                        </div>
                                    </div>
                                </div>

                                <div id="yearly-sales-collapse" class="collapse show">
                                    <div class="table-responsive">
                                        <table class="table table-nowrap table-hover mb-0">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Username</th>
                                                <th>E-mail</th>
                                                <th>Role</th>
                                                <th>Actions</th>
                                            </tr>
                                            </thead>
                                            <tbody id="tableBody">
                                            <%
                                                List<User> userList = (List<User>) request.getAttribute("filteredUsers");
                                                if (userList != null && !userList.isEmpty()) {
                                                    for (User user : userList) {
                                            %>
                                            <tr>
                                                <td><%= user.getId() %></td>
                                                <td><%= user.getUsername() %></td>
                                                <td><%= user.getEmail() %></td>
                                                <td>
                                                    <%= (user.getRole() != null) ? user.getRole().toString() : "No Role" %>
                                                </td>
                                                <td>
                                                    <a href="users?action=edit&id=<%= user.getId() %>" class="btn btn-sm btn-primary">Edit</a>

                                                    <form action="users" method="POST" class="d-inline">
                                                        <input type="hidden" name="id" value="<%= user.getId() %>"/>
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
                                                <td colspan="5" class="text-center">No users found.</td>
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



