<%@include file="../partials/sessionCheck.jsp"%>

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
                                    <li class="breadcrumb-item"><a href="javascript:void(0);">Dashboard </a></li>
                                    <li class="breadcrumb-item active">Tags !</li>
                                </ol>
                            </div>
                            <h4 class="page-title">Create a tag here</h4>
                        </div>
                    </div>
                </div>
                <!-- end page title -->

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="header-title">Add a new tag</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <%
                                            String errorMessages = (String) request.getAttribute("errorMessages");
                                            if (errorMessages != null && !errorMessages.isEmpty()) {
                                        %>
                                        <div style="color:red;">
                                            <%= errorMessages %>
                                        </div>
                                        <%
                                            }
                                        %>

                                        <form action="${pageContext.request.contextPath}/manager/tags" method="POST" id="addTagForm">
                                            <div class="mb-3">
                                                <label for="name" class="form-label">Tag Name</label>
                                                <input type="text" id="name" class="form-control" name="name" placeholder="Enter tag name" >
                                            </div>

                                            <button type="submit" id="submitButton" class="btn btn-primary" name="addTag">Submit</button>
                                            <a href="tags" class="btn btn-secondary">Go Back</a>
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



