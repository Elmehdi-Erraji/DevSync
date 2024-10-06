<!-- ========== Left Sidebar Start ========== -->
<div class="leftside-menu">

    <!-- Brand Logo Light -->
    <a href="" class="logo logo-light">
        <span class="logo-lg">
            <img src="${pageContext.request.contextPath}/images/logo-sm.png" alt="logo">
        </span>
        <span class="logo-sm">
            <img src="${pageContext.request.contextPath}/images/logo-sm.png" alt="small logo">
        </span>
    </a>

    <!-- Brand Logo Dark -->
    <a href="index.html" class="logo logo-dark">
        <span class="logo-lg">
            <img src="${pageContext.request.contextPath}/images/logo-sm.png" alt="dark logo">
        </span>
        <span class="logo-sm">
            <img src="${pageContext.request.contextPath}/images/logo-sm.png" alt="small logo">
        </span>
    </a>

    <!-- Sidebar -left -->
    <div class="h-100" id="leftside-menu-container" data-simplebar>
        <!--- Sidemenu -->
        <ul class="side-nav">

            <li class="side-nav-title">Management</li>

            <!-- Home Link -->
            <li class="side-nav-item">
                <a href="${pageContext.request.contextPath}/user/tasks" class="side-nav-link">
                    <i class="ri-home-line"></i>
                    <span>Home</span>
                </a>
            </li>

            <!-- Request Management -->
            <li class="side-nav-item">
                <a data-bs-toggle="collapse" href="#requestManagement" aria-expanded="false" aria-controls="requestManagement" class="side-nav-link">
                    <i class="ri-mail-check-line"></i>
                    <span>Request Management</span>
                    <span class="menu-arrow"></span>
                </a>
                <div class="collapse" id="requestManagement">
                    <ul class="side-nav-second-level">
                        <li><a href="#">Requests List</a></li>
                    </ul>
                </div>
            </li>

        </ul>
    </div>
</div>
<!-- ========== Left Sidebar End ========== -->
