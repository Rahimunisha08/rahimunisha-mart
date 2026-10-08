<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 24px;">Your Order History</h1>

<c:if test="${param.cancelled == 'true'}">
    <div class="alert alert-success">Order has been cancelled successfully.</div>
</c:if>

<c:choose>
    <c:when test="${empty orders}">
        <div style="background: white; padding: 60px 20px; text-align: center; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
            <div style="font-size: 3rem; margin-bottom: 12px;">📦</div>
            <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 8px;">No past orders yet</h2>
            <p style="color: var(--gray-500); margin-bottom: 20px;">Once you place an order, track status and receipts here!</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Start Shopping</a>
        </div>
    </c:when>
    <c:otherwise>
        <div style="display: flex; flex-direction: column; gap: 20px;">
            <c:forEach var="order" items="${orders}">
                <div style="background: white; border-radius: var(--border-radius); box-shadow: var(--card-shadow); padding: 24px; border: 1px solid var(--gray-200);">
                    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--gray-200); padding-bottom: 14px; margin-bottom: 16px;">
                        <div>
                            <span style="font-weight: 800; font-size: 1.1rem; color: var(--primary);">Order #${order.id}</span>
                            <span style="color: var(--gray-500); font-size: 0.85rem; margin-left: 12px;">
                                Placed on <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                            </span>
                        </div>
                        <div>
                            <span class="status-badge status-${order.status}"><c:out value="${order.status}" /></span>
                        </div>
                    </div>

                    <!-- Items in this Order -->
                    <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 16px;">
                        <c:forEach var="item" items="${order.items}">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <div style="display: flex; align-items: center; gap: 12px;">
                                    <img src="<c:out value='${item.productImageUrl}' />" alt="<c:out value='${item.productName}' />" 
                                         style="width: 45px; height: 45px; object-fit: cover; border-radius: 6px;"
                                         onerror="this.src='https://via.placeholder.com/45'">
                                    <div>
                                        <a href="${pageContext.request.contextPath}/products/${item.productId}" style="font-weight: 600; font-size: 0.95rem;">
                                            <c:out value="${item.productName}" />
                                        </a>
                                        <div style="font-size: 0.8rem; color: var(--gray-500);">Qty: ${item.quantity} × ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" /></div>
                                    </div>
                                </div>
                                <div style="font-weight: 700;">
                                    ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00" />
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Footer of Order Card -->
                    <div style="display: flex; justify-content: space-between; align-items: center; border-top: 1px solid var(--gray-100); padding-top: 14px;">
                        <div>
                            <span style="font-size: 0.85rem; color: var(--gray-500);">Shipping Address: </span>
                            <span style="font-size: 0.85rem; font-weight: 500;"><c:out value="${order.shippingAddress}" /></span>
                        </div>
                        <div style="display: flex; align-items: center; gap: 16px;">
                            <span style="font-size: 1.1rem; font-weight: 800;">
                                Total: ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" />
                            </span>

                            <a href="${pageContext.request.contextPath}/orders/${order.id}" class="btn btn-outline btn-sm">View Details</a>

                            <c:if test="${order.status == 'PENDING'}">
                                <form action="${pageContext.request.contextPath}/orders/${order.id}/cancel" method="post" style="display: inline;">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Cancel this order?');">Cancel</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
