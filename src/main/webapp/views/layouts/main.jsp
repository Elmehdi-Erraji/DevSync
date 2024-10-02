<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
{pageContext.request.contextPath}/css/app.min.css
<script src="${pageContext.request.contextPath}/js/app.min.js"></script>

<head>
    <meta charset="utf-8" />
    <title>Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="fully responsive." name="description" />
    <meta content="Mehdi" name="author" />

    <!-- App favicon -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico">

    <!-- Theme Config Js -->
    <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>

    <!-- App css -->
    <link href="${pageContext.request.contextPath}/assets/css/app.min.css" rel="stylesheet" type="text/css" id="app-style" />

    <!-- Icons css -->
    <link href="${pageContext.request.contextPath}/assets/css/icons.min.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="wrapper">
    <jsp:include page="//includes/topbar.jsp" />
    <jsp:include page="//includes/sidebar.jsp" />

    <div class="content-page">
        <div class="content">
            <!-- Placeholder for dynamic content -->
            <jsp:include page="${contentPage}" />
        </div>
    </div>
</div>

<footer class="footer">
    <div class="container-fluid">
        <div class="row">
            <div class="col-12 text-center">
                <script>
                    document.write(new Date().getFullYear())
                </script> © Created by <b>Mehdi</b>
            </div>
        </div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}./js/vendor.min.js"></script>
<script src="${pageContext.request.contextPath}./js/app.min.js"></script>

</body>
</html>
