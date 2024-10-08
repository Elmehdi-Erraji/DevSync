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

            <!-- User Management -->
            <li class="side-nav-item">
                <a data-bs-toggle="collapse" href="#userManagement" aria-expanded="false" aria-controls="userManagement" class="side-nav-link">
                    <i class="ri-group-line"></i>
                    <span>User Management</span>
                    <span class="menu-arrow"></span>
                </a>
                <div class="collapse" id="userManagement">
                    <ul class="side-nav-second-level">
                        <li><a href="users?action=create">Create A User</a></li>
                        <li><a href="users">Users List</a></li>
                    </ul>
                </div>
            </li>

            <!-- Tag Management -->
            <li class="side-nav-item">
                <a data-bs-toggle="collapse" href="#tagManagement" aria-expanded="false" aria-controls="tagManagement" class="side-nav-link">
                    <i class="ri-price-tag-3-line"></i>
                    <span>Tag Management</span>
                    <span class="menu-arrow"></span>
                </a>
                <div class="collapse" id="tagManagement">
                    <ul class="side-nav-second-level">
                        <li><a href="tags?action=create">Create A Tag</a></li>
                        <li><a href="tags">Tags List</a></li>
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
                        <li><a href="tasks?action=create">Create A Task</a></li>
                        <li><a href="tasks">Tasks List</a></li>
                    </ul>
                </div>
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
                        <li><a href="request">Requests List</a></li>
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

        </ul>
    </div>
</div>
<!-- ========== Left Sidebar End ========== -->
