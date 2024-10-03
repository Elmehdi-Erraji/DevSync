<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <title>Dashboard </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="Mehdi" name="author" />

    <!-- App favicon -->
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">

    <!-- Theme Config Js -->
    <script src="${pageContext.request.contextPath}/js/config.js"></script>

    <!-- App css -->
    <link href="${pageContext.request.contextPath}/css/app.min.css" rel="stylesheet" type="text/css" id="app-style" />

    <!-- Icons css -->
    <link href="${pageContext.request.contextPath}/css/icons.min.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="wrapper">

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <!-- ========== Topbar Start ========== -->
    <div class="navbar-custom">
        <div class="topbar container-fluid">
            <div class="d-flex align-items-center gap-1">

                <!-- Topbar Brand Logo -->
                <div class="logo-topbar"style="color: red">
                    <!-- Logo light -->
                    <a href="" class="logo-light" >
                                <span class="logo-lg">
                                    <img src="images/logo-sm.png"  alt="">
                                </span>

                    </a>


                </div>

                <!-- Sidebar Menu Toggle Button -->
                <button class="button-toggle-menu">
                    <i class="ri-menu-line"></i>
                </button>

                <!-- Horizontal Menu Toggle Button -->
                <button class="navbar-toggle" data-bs-toggle="collapse" data-bs-target="#topnav-menu-content">
                    <div class="lines">
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                </button>

            </div>

            <ul class="topbar-menu d-flex align-items-center gap-3">


                <li class="d-none d-sm-inline-block">
                    <div class="nav-link" id="light-dark-mode">
                        <i class="ri-moon-line fs-22"></i>
                    </div>
                </li>

                <li class="dropdown">
                    <a class="nav-link dropdown-toggle arrow-none nav-user" data-bs-toggle="dropdown" href="#" role="button"
                       aria-haspopup="false" aria-expanded="false">
                                <span class="account-user-avatar">
                                    <img src="images/users/avatar-1.jpg" alt="user-image" width="32" class="rounded-circle">
                                </span>
                        <span class="d-lg-block d-none">
                                    <h5 class="my-0 fw-normal">Thomson <i
                                            class="ri-arrow-down-s-line d-none d-sm-inline-block align-middle"></i></h5>
                                </span>
                    </a>
                    <div class="dropdown-menu dropdown-menu-end dropdown-menu-animated profile-dropdown">
                        <!-- item-->
                        <div class=" dropdown-header noti-title">
                            <h6 class="text-overflow m-0">Welcome !</h6>
                        </div>

                        <!-- item-->
                        <a href="pages-profile.html" class="dropdown-item">
                            <i class="ri-account-circle-line fs-18 align-middle me-1"></i>
                            <span>My Account</span>
                        </a>

                        <!-- item-->
                        <a href="auth-logout-2.html" class="dropdown-item">
                            <i class="ri-logout-box-line fs-18 align-middle me-1"></i>
                            <span>Logout</span>
                        </a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <!-- ========== Topbar End ========== -->

    <!-- ========== Left Sidebar Start ========== -->
    <div class="leftside-menu">

        <!-- Brand Logo Light -->
        <a href="" class="logo logo-light">
        <span class="logo-lg">
            <img src="images/logo-sm.png" alt="logo">
        </span>
            <span class="logo-sm">
            <img src="images/logo-sm.png" alt="small logo">
        </span>
        </a>

        <!-- Brand Logo Dark -->
        <a href="index.html" class="logo logo-dark">
        <span class="logo-lg">
            <img src="images/logo-sm.png" alt="dark logo">
        </span>
            <span class="logo-sm">
            <img src="images/logo-sm.png" alt="small logo">
        </span>
        </a>

        <!-- Sidebar -left -->
        <div class="h-100" id="leftside-menu-container" data-simplebar>
            <!--- Sidemenu -->
            <ul class="side-nav">

                <li class="side-nav-title">Management</li>

                <!-- User Management -->
                <li class="side-nav-item">
                    <a data-bs-toggle="collapse" href="#userManagement" aria-expanded="false" aria-controls="userManagement" class="side-nav-link">
                        <i class="ri-group-line"></i>
                        <span>User Management</span>
                        <span class="menu-arrow"></span>
                    </a>
                    <div class="collapse" id="userManagement">
                        <ul class="side-nav-second-level">
                            <li><a href="#">Create User</a></li>
                            <li><a href="#">Read User</a></li>
                            <li><a href="#">Update User</a></li>
                            <li><a href="#">Delete User</a></li>
                        </ul>
                    </div>
                </li>

                <!-- Task Management -->
                <li class="side-nav-item">
                    <a data-bs-toggle="collapse" href="#taskManagement" aria-expanded="false" aria-controls="taskManagement" class="side-nav-link">
                        <i class="ri-task-line"></i>
                        <span>Task Management</span>
                        <span class="menu-arrow"></span>
                    </a>
                    <div class="collapse" id="taskManagement">
                        <ul class="side-nav-second-level">
                            <li><a href="#">Create Task</a></li>
                            <li><a href="#">Read Task</a></li>
                            <li><a href="#">Update Task</a></li>
                            <li><a href="#">Delete Task</a></li>
                        </ul>
                    </div>
                </li>

                <!-- Token Management -->
                <li class="side-nav-item">
                    <a data-bs-toggle="collapse" href="#tokenManagement" aria-expanded="false" aria-controls="tokenManagement" class="side-nav-link">
                        <i class="ri-key-line"></i>
                        <span>Token Management</span>
                        <span class="menu-arrow"></span>
                    </a>
                    <div class="collapse" id="tokenManagement">
                        <ul class="side-nav-second-level">
                            <li><a href="#">Create Token</a></li>
                            <li><a href="#">Read Token</a></li>
                            <li><a href="#">Update Token</a></li>
                            <li><a href="#">Delete Token</a></li>
                        </ul>
                    </div>
                </li>

                <!-- Project Management -->
                <li class="side-nav-item">
                    <a data-bs-toggle="collapse" href="#projectManagement" aria-expanded="false" aria-controls="projectManagement" class="side-nav-link">
                        <i class="ri-folder-line"></i>
                        <span>Project Management</span>
                        <span class="menu-arrow"></span>
                    </a>
                    <div class="collapse" id="projectManagement">
                        <ul class="side-nav-second-level">
                            <li><a href="#">Create Project</a></li>
                            <li><a href="#">Read Project</a></li>
                            <li><a href="#">Update Project</a></li>
                            <li><a href="#">Delete Project</a></li>
                        </ul>
                    </div>
                </li>

                <!-- Notification Management -->
                <li class="side-nav-item">
                    <a data-bs-toggle="collapse" href="#notificationManagement" aria-expanded="false" aria-controls="notificationManagement" class="side-nav-link">
                        <i class="ri-notification-line"></i>
                        <span>Notification Management</span>
                        <span class="menu-arrow"></span>
                    </a>
                    <div class="collapse" id="notificationManagement">
                        <ul class="side-nav-second-level">
                            <li><a href="#">Create Notification</a></li>
                            <li><a href="#">Read Notification</a></li>
                            <li><a href="#">Update Notification</a></li>
                            <li><a href="#">Delete Notification</a></li>
                        </ul>
                    </div>
                </li>

            </ul>
        </div>
    </div>

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
                            <h4 class="page-title">Create a user here</h4>
                        </div>
                    </div>
                </div>
                <!-- end page title -->

                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="header-title">Add a new user</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-lg-6">

                                        <form action="${pageContext.request.contextPath}/manager/tags" method="POST" id="addTagForm">
                                            <div class="mb-3">
                                                <label for="name" class="form-label">Tag Name</label>
                                                <input type="text" id="name" class="form-control" name="name" placeholder="Enter tag name" required>
                                            </div>

                                            <button type="submit" id="submitButton" class="btn btn-primary" name="addTag">Submit</button>
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
        <footer class="footer">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12 text-center"> 2024 @ <b>Mehdi</b>
                    </div>
                </div>
            </div>
        </footer>
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



