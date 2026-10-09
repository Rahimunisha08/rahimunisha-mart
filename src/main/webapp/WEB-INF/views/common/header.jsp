<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'RahimunishaMart — Premier E-Commerce'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/chatbot.css">
    <script>
        window.APP_CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
</head>
<body>

<header class="navbar">
    <div class="container navbar-content">
        <a href="${pageContext.request.contextPath}/home" class="logo">
            🛒 Rahimunisha<span>Mart</span>
        </a>

        <form action="${pageContext.request.contextPath}/home" method="get" class="search-bar">
            <input type="text" name="q" class="search-input" placeholder="Search electronics, fashion, lifestyle..." value="<c:out value='${param.q}' />">
            <button type="submit" class="btn btn-primary btn-sm">Search</button>
        </form>

        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/home" class="nav-link">Shop</a></li>

            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <c:if test="${sessionScope.currentUser.role == 'BUYER'}">
                        <li><a href="${pageContext.request.contextPath}/wishlist" class="nav-link">❤️ Wishlist</a></li>
                        <li><a href="${pageContext.request.contextPath}/orders" class="nav-link">📦 My Orders</a></li>
                        <li>
                            <a href="${pageContext.request.contextPath}/cart" class="nav-link">
                                🛒 Cart <span id="cartBadge" class="badge">0</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'SELLER'}">
                        <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link">📊 Seller Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/seller/products/new" class="nav-link">➕ Add Product</a></li>
                        <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link">📦 Orders</a></li>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">⚙️ Admin Panel</a></li>
                    </c:if>

                    <li>
                        <span class="nav-link" style="color: var(--primary); font-weight: 700;">
                            👋 <c:out value="${sessionScope.currentUser.fullName}" />
                        </span>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-outline btn-sm">Logout</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/wishlist" class="nav-link">❤️ Wishlist</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart" class="nav-link">🛒 Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth/login" class="nav-link">Sign In</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth/register" class="btn btn-primary btn-sm">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</header>
<main class="container" style="padding-top: 24px; padding-bottom: 40px; flex: 1;">
