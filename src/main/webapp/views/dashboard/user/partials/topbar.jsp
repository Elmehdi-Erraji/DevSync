<!-- ========== Topbar Start ========== -->
<div class="navbar-custom">
    <div class="topbar container-fluid">
        <div class="d-flex align-items-center gap-1">

            <!-- Topbar Brand Logo -->
            <div class="logo-topbar" style="color: red">
                <a href="" class="logo-light">
                    <span class="logo-lg">
                        <img src="${pageContext.request.contextPath}/images/logo-sm.png" alt="">
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

            <!-- Token Display Section -->
            <div class="ms-auto d-flex gap-2">
                <button class="btn btn-success text-white">
                    Daily Tokens: <span class="fw-bold"><%= session.getAttribute("dailyTokens") != null ? session.getAttribute("dailyTokens") : 0 %></span>
                </button>
                <button class="btn btn-warning text-white">
                    Monthly Tokens: <span class="fw-bold"><%= session.getAttribute("monthlyTokens") != null ? session.getAttribute("monthlyTokens") : 0 %></span>
                </button>
            </div>

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
                        <img src="${pageContext.request.contextPath}/images/users/avatar-1.jpg" alt="user-image" width="32" class="rounded-circle">
                    </span>
                    <span class="d-lg-block d-none">
                        <h5 class="my-0 fw-normal">
                            <%= session.getAttribute("username") != null ? session.getAttribute("username") : "Guest" %>
                            <i class="ri-arrow-down-s-line d-none d-sm-inline-block align-middle"></i>
                        </h5>
                    </span>
                </a>
                <div class="dropdown-menu dropdown-menu-end dropdown-menu-animated profile-dropdown">
                    <div class="dropdown-header noti-title">
                        <h6 class="text-overflow m-0">Welcome!</h6>
                    </div>
                    <a href="#" class="dropdown-item">
                        <i class="ri-account-circle-line fs-18 align-middle me-1"></i>
                        <span>My Account</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">
                        <i class="ri-logout-box-line fs-18 align-middle me-1"></i>
                        <span>Logout</span>
                    </a>
                </div>
            </li>
        </ul>
    </div>
</div>
<!-- ========== Topbar End ========== -->
