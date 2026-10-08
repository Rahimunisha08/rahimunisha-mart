<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="form-card" style="max-width: 540px;">
    <h1 style="font-size: 1.6rem; font-weight: 800; text-align: center; margin-bottom: 8px;">Create Account</h1>
    <p style="text-align: center; color: var(--gray-500); font-size: 0.9rem; margin-bottom: 24px;">Join RahimunishaMart as a Buyer or Seller</p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/register" method="post">
        <div class="form-group">
            <label class="form-label" for="fullName">Full Name:</label>
            <input type="text" id="fullName" name="fullName" class="form-control" 
                   value="<c:out value='${enteredFullName}' />" placeholder="John Doe" required>
            <c:if test="${not empty fieldErrors.fullName}">
                <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.fullName}" /></span>
            </c:if>
        </div>

        <div class="form-group">
            <label class="form-label" for="email">Email Address:</label>
            <input type="email" id="email" name="email" class="form-control" 
                   value="<c:out value='${enteredEmail}' />" placeholder="john@example.com" required>
            <c:if test="${not empty fieldErrors.email}">
                <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.email}" /></span>
            </c:if>
        </div>

        <div class="form-group">
            <label class="form-label" for="password">Password (min 6 characters):</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            <c:if test="${not empty fieldErrors.password}">
                <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.password}" /></span>
            </c:if>
        </div>

        <div class="form-group">
            <label class="form-label" for="role">Account Role:</label>
            <select id="role" name="role" class="form-control" required>
                <option value="BUYER" ${enteredRole == 'BUYER' ? 'selected' : ''}>Buyer (Purchase products & leave reviews)</option>
                <option value="SELLER" ${enteredRole == 'SELLER' ? 'selected' : ''}>Seller (List products & fulfill incoming orders)</option>
            </select>
            <small style="color: var(--gray-500); font-size: 0.8rem;">
                * Note: Admin accounts are pre-seeded per specification guidelines.
            </small>
            <c:if test="${not empty fieldErrors.role}">
                <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.role}" /></span>
            </c:if>
        </div>

        <div class="form-group">
            <label class="form-label" for="phone">Phone Number:</label>
            <input type="text" id="phone" name="phone" class="form-control" 
                   value="<c:out value='${enteredPhone}' />" placeholder="+91 9876543210">
        </div>

        <div class="form-group">
            <label class="form-label" for="address">Shipping / Business Address:</label>
            <textarea id="address" name="address" rows="2" class="form-control" placeholder="Street, City, Pincode"><c:out value="${enteredAddress}" /></textarea>
        </div>

        <button type="submit" class="btn btn-primary btn-block" style="padding: 12px; margin-top: 10px;">
            Register Account
        </button>
    </form>

    <p style="text-align: center; font-size: 0.9rem; margin-top: 20px;">
        Already registered? <a href="${pageContext.request.contextPath}/auth/login">Sign in here</a>
    </p>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
