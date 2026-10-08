<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="form-card">
    <h1 style="font-size: 1.6rem; font-weight: 800; text-align: center; margin-bottom: 8px;">Welcome Back</h1>
    <p style="text-align: center; color: var(--gray-500); font-size: 0.9rem; margin-bottom: 24px;">Sign in to RahimunishaMart</p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
    </c:if>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success"><c:out value="${successMessage}" /></div>
    </c:if>

    <c:if test="${param.logout == 'true'}">
        <div class="alert alert-success">You have been logged out safely.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/login" method="post">
        <input type="hidden" name="redirect" value="<c:out value='${param.redirect}' />">

        <div class="form-group">
            <label class="form-label" for="email">Email Address:</label>
            <input type="email" id="email" name="email" class="form-control" 
                   value="<c:out value='${enteredEmail != null ? enteredEmail : param.email}' />" 
                   placeholder="name@example.com" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="password">Password:</label>
            <input type="password" id="password" name="password" class="form-control" 
                   placeholder="••••••••" required>
        </div>

        <button type="submit" class="btn btn-primary btn-block" style="padding: 12px; margin-top: 10px;">
            Sign In
        </button>
    </form>

    <!-- Demo Account Quick Fill Buttons for Evaluator Review -->
    <div style="margin-top: 24px; padding-top: 16px; border-top: 1px solid var(--gray-200);">
        <p style="font-size: 0.8rem; font-weight: 700; color: var(--gray-500); margin-bottom: 8px; text-transform: uppercase;">
            Demo Quick Login (Click to fill):
        </p>
        <div style="display: flex; gap: 6px; flex-wrap: wrap;">
            <button type="button" class="btn btn-outline btn-sm" onclick="fillCreds('nisha@rahimunishamart.com', 'Password@123')">
                👤 Buyer
            </button>
            <button type="button" class="btn btn-outline btn-sm" onclick="fillCreds('techseller@rahimunishamart.com', 'Password@123')">
                🏪 Seller
            </button>
            <button type="button" class="btn btn-outline btn-sm" onclick="fillCreds('admin@rahimunishamart.com', 'Password@123')">
                🛡️ Admin
            </button>
        </div>
    </div>

    <p style="text-align: center; font-size: 0.9rem; margin-top: 20px;">
        Don't have an account? <a href="${pageContext.request.contextPath}/auth/register">Create one here</a>
    </p>
</div>

<script>
function fillCreds(email, password) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = password;
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
