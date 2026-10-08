<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<c:if test="${not empty successMessage}">
    <div class="alert alert-success"><c:out value="${successMessage}" /></div>
</c:if>

<div style="background: white; border-radius: var(--border-radius); box-shadow: var(--card-shadow); padding: 32px; max-width: 800px; margin: 0 auto;">
    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid var(--gray-200); padding-bottom: 18px; margin-bottom: 24px;">
        <div>
            <h1 style="font-size: 1.6rem; font-weight: 800;">Receipt & Tracking: Order #${order.id}</h1>
            <p style="color: var(--gray-500); font-size: 0.85rem;">Date: <fmt:formatDate value="${order.createdAt}" pattern="MMMM dd, yyyy HH:mm:ss" /></p>
        </div>
        <div>
            <span class="status-badge status-${order.status}"><c:out value="${order.status}" /></span>
        </div>
    </div>

    <!-- Status Tracking Timeline (O2) -->
    <div style="display: flex; justify-content: space-between; margin-bottom: 30px; position: relative; padding: 0 10px;">
        <div style="text-align: center;">
            <div style="width: 32px; height: 32px; border-radius: 50%; background: var(--primary); color: white; display: flex; align-items: center; justify-content: center; margin: 0 auto 6px; font-weight: 700;">✓</div>
            <span style="font-size: 0.85rem; font-weight: 600;">Placed</span>
        </div>
        <div style="text-align: center;">
            <div style="width: 32px; height: 32px; border-radius: 50%; background: ${order.status != 'PENDING' && order.status != 'CANCELLED' ? 'var(--primary)' : 'var(--gray-300)'}; color: white; display: flex; align-items: center; justify-content: center; margin: 0 auto 6px; font-weight: 700;">
                ${order.status != 'PENDING' && order.status != 'CANCELLED' ? '✓' : '2'}
            </div>
            <span style="font-size: 0.85rem; font-weight: 600;">Confirmed</span>
        </div>
        <div style="text-align: center;">
            <div style="width: 32px; height: 32px; border-radius: 50%; background: ${order.status == 'SHIPPED' || order.status == 'DELIVERED' ? 'var(--primary)' : 'var(--gray-300)'}; color: white; display: flex; align-items: center; justify-content: center; margin: 0 auto 6px; font-weight: 700;">
                ${order.status == 'SHIPPED' || order.status == 'DELIVERED' ? '✓' : '3'}
            </div>
            <span style="font-size: 0.85rem; font-weight: 600;">Shipped</span>
        </div>
        <div style="text-align: center;">
            <div style="width: 32px; height: 32px; border-radius: 50%; background: ${order.status == 'DELIVERED' ? 'var(--success)' : 'var(--gray-300)'}; color: white; display: flex; align-items: center; justify-content: center; margin: 0 auto 6px; font-weight: 700;">
                ${order.status == 'DELIVERED' ? '✓' : '4'}
            </div>
            <span style="font-size: 0.85rem; font-weight: 600;">Delivered</span>
        </div>
    </div>

    <!-- Itemized List -->
    <h2 style="font-size: 1.15rem; font-weight: 700; margin-bottom: 14px;">Items Purchased</h2>
    <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 24px;">
        <c:forEach var="item" items="${order.items}">
            <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--gray-100); padding-bottom: 12px;">
                <div style="display: flex; align-items: center; gap: 14px;">
                    <img src="<c:out value='${item.productImageUrl}' />" alt="<c:out value='${item.productName}' />" 
                         style="width: 50px; height: 50px; object-fit: cover; border-radius: 6px;"
                         onerror="this.src='https://via.placeholder.com/50'">
                    <div>
                        <a href="${pageContext.request.contextPath}/products/${item.productId}" style="font-weight: 600;">
                            <c:out value="${item.productName}" />
                        </a>
                        <div style="font-size: 0.8rem; color: var(--gray-500);">
                            Qty: ${item.quantity} × ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" />
                        </div>
                    </div>
                </div>
                <div style="font-weight: 700;">
                    ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00" />
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Summary Details -->
    <div style="background: #f8fafc; padding: 20px; border-radius: 8px; margin-bottom: 24px;">
        <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
            <span>Payment Method:</span>
            <strong><c:out value="${order.paymentMethod}" /> (<c:out value="${order.paymentStatus}" />)</strong>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
            <span>Delivery Address:</span>
            <strong><c:out value="${order.shippingAddress}" /></strong>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
            <span>Customer Name:</span>
            <strong><c:out value="${order.buyerName}" /> (<c:out value="${order.buyerEmail}" />)</strong>
        </div>
        <hr style="border: none; border-top: 1px solid var(--gray-200); margin: 12px 0;">
        <div style="display: flex; justify-content: space-between; font-size: 1.25rem; font-weight: 800;">
            <span>Total Paid:</span>
            <span style="color: var(--primary);">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" /></span>
        </div>
    </div>

    <div style="display: flex; justify-content: space-between;">
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline btn-sm">⬅ Back to Order History</a>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-sm">Continue Shopping</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
