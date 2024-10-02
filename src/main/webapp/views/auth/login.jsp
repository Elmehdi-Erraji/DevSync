<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Log In</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">

    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">

    <!-- Link to CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/app.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/icons.min.css">



    <!-- JavaScript Files -->
    <script src="${pageContext.request.contextPath}/js/app.min.js"></script>
</head>
<body class="authentication-bg position-relative">
<div class="account-pages pt-2 pt-sm-5 pb-4 pb-sm-5 position-relative">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-xxl-8 col-lg-10">
                <div class="card overflow-hidden">
                    <div class="row g-0">
                        <div class="col-lg-6 d-none d-lg-block p-2">
                            <img src="${pageContext.request.contextPath}/images/authpic.jpg" alt="" class="img-fluid rounded h-100">
                        </div>
                        <div class="col-lg-6">
                            <div class="d-flex flex-column h-100">
                                <div class="p-4 my-auto">
                                    <h4 class="fs-20">Sign In</h4>
                                    <p class="text-muted mb-3">Enter your email address and password to access account.</p>

                                    <!-- Login Form -->
                                    <form method="POST" action="${pageContext.request.contextPath}/login">
                                        <div class="mb-3">
                                            <label for="email" class="form-label">Email address</label>
                                            <input class="form-control" type="email" name="email" id="email" placeholder="Enter your email" required>
                                        </div>
                                        <div class="mb-3">
                                            <a href="${pageContext.request.contextPath}/password/request" class="text-muted float-end"><small>Forgot your password?</small></a>
                                            <label for="password" class="form-label">Password</label>
                                            <input class="form-control" type="password" name="password" id="password" placeholder="Enter your password" required>
                                        </div>
                                        <div class="mb-0 text-start">
                                            <button class="btn btn-soft-primary w-100" type="submit"><i class="ri-login-circle-fill me-1"></i> <span class="fw-bold">Log In</span> </button>
                                        </div>
                                    </form>
                                    <!-- End Login Form -->
                                </div>
                            </div>
                        </div> <!-- end col -->
                    </div>
                </div>
            </div>
            <!-- end row -->
        </div>
        <div class="row">
            <div class="col-12 text-center">
                <p class="text-dark-emphasis">Don't have an account? <a href="${pageContext.request.contextPath}/register" class="text-dark fw-bold ms-1 link-offset-3 text-decoration-underline"><b>Sign up</b></a></p>
                <p class="text-dark-emphasis">Go back <a href="${pageContext.request.contextPath}/home" class="text-dark fw-bold ms-1 link-offset-3 text-decoration-underline"><b>Home</b></a></p>
            </div> <!-- end col -->
        </div>
        <!-- end row -->
    </div>


    <!-- end container -->
</div>

<footer class="footer footer-alt fw-medium">
    <span class="text-dark">
        <script>
            document.write(new Date().getFullYear())
        </script> © Mehdi
    </span>
</footer>

</body>
</html>
