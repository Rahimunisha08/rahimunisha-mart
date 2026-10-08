<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
    <div>
        <h1 style="font-size: 1.8rem; font-weight: 800;">Incoming Seller Orders</h1>
        <p style="color: var(--gray-500);">Fulfill customer orders and progress order workflow status (O2).</p>
    </div>
    <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-outline btn-sm">⬅ Back to Dashboard</a>
</div>

<c:if test="${param.updated == 'true'}">
    <div class="alert alert-success">Order fulfillment status updated successfully.</div>
</c:if>

<c:choose>
    <c:when test="${empty orders}">
        <div style="background: white; padding: 60px 20px; text-align: center; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
            <div style="font-size: 3rem; margin-bottom: 12px;">📦</div>
            <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 8px;">No customer orders placed yet</h2>
            <p style="color: var(--gray-500);">New customer orders containing your products will show up here for shipping.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div style="display: flex; flex-direction: column; gap: 20px;">
            <c:forEach var="order" items="${orders}">
                <div style="background: white; border-radius: var(--border-radius); box-shadow: var(--card-shadow); padding: 24px;">
                    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--gray-200); padding-bottom: 14px; margin-bottom: 16px;">
                        <div>
                            <span style="font-weight: 800; font-size: 1.1rem; color: var(--primary);">Order #${order.id}</span>
                            <span style="color: var(--gray-500); font-size: 0.85rem; margin-left: 10px;">
                                Buyer: <strong><c:out value="${order.buyerName}" /></strong> (<c:out value="${order.buyerEmail}" />)
                            </span>
                        </div>
                        <div>
                            <span class="status-badge status-${order.status}"><c:out value="${order.status}" /></span>
                        </div>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 10px; margin-bottom: 16px;">
                        <c:forEach var="item" items="${order.items}">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <div>
                                    <span style="font-weight: 600;"><c:out value="${item.productName}" /></span>
                                    <span style="color: var(--gray-500); font-size: 0.85rem;"> × ${item.quantity}</span>
                                </div>
                                <div style="font-weight: 700;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00" /></div>
                            </div>
                        </c:forEach>
                    </div>

                    <div style="background: #f8fafc; padding: 12px 16px; border-radius: 6px; font-size: 0.85rem; margin-bottom: 16px;">
                        <strong>Shipping Destination:</strong> <c:out value="${order.shippingAddress}" />
                    </div>

                    <!-- Workflow Status Transition Form (O2) -->
                    <div style="display: flex; justify-content: space-between; align-items: center; border-top: 1px solid var(--gray-100); padding-top: 14px;">
                        <div style="font-weight: 800; font-size: 1.1rem;">
                            Total: ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" />
                        </div>

                        <form action="${pageContext.request.contextPath}/seller/orders/status" method="post" style="display: flex; gap: 8px; align-items: center;">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <label style="font-size: 0.85rem; font-weight: 600;">Advance Status:</label>
                            <select name="status" class="form-control" style="width: auto; padding: 6px 10px; font-size: 0.85rem;">
                                <option value="CONFIRMED" ${order.status == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                            </select>
                            <button type="submit" class="btn btn-primary btn-sm">Update</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
