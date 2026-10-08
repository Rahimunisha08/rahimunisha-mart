<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="background: white; padding: 60px 20px; text-align: center; border-radius: var(--border-radius); box-shadow: var(--card-shadow); max-width: 600px; margin: 40px auto;">
    <div style="font-size: 3.5rem; margin-bottom: 12px;">🚫</div>
    <h1 style="font-size: 1.8rem; font-weight: 800; color: var(--danger); margin-bottom: 8px;">403 — Access Denied</h1>
    <p style="color: var(--gray-500); margin-bottom: 24px;">You do not possess the required permissions to access this protected area.</p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Return to Storefront</a>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
