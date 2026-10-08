<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 24px;">Your Shopping Cart</h1>

<c:if test="${not empty sessionScope.cartError}">
    <div class="alert alert-error">
        <c:out value="${sessionScope.cartError}" />
    </div>
    <c:remove var="cartError" scope="session" />
</c:if>

<c:if test="${param.empty == 'true'}">
    <div class="alert alert-error">Your cart is empty. Please add items before checking out.</div>
</c:if>

<c:choose>
    <c:when test="${empty cartItems}">
        <div style="background: white; padding: 60px 20px; text-align: center; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
            <div style="font-size: 3rem; margin-bottom: 12px;">🛒</div>
            <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 8px;">Your cart is currently empty</h2>
            <p style="color: var(--gray-500); margin-bottom: 24px;">Browse our wide collection of electronics and fashion items!</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Start Shopping</a>
        </div>
    </c:when>
    <c:otherwise>
        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 30px; align-items: start;">
            <!-- Cart Items Table -->
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Unit Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cartItems}">
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 12px;">
                                        <img src="<c:out value='${item.product.imageUrl}' />" alt="<c:out value='${item.product.name}' />" 
                                             style="width: 50px; height: 50px; object-fit: cover; border-radius: 6px;"
                                             onerror="this.src='https://via.placeholder.com/50'">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/products/${item.product.id}" style="font-weight: 600;">
                                                <c:out value="${item.product.name}" />
                                            </a>
                                            <div style="font-size: 0.8rem; color: var(--gray-500);"><c:out value="${item.product.category}" /></div>
                                        </div>
                                    </div>
                                </td>
                                <td>₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00" /></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart/update" method="post" style="display: flex; gap: 4px; align-items: center;">
                                        <input type="hidden" name="cartItemId" value="${item.id}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stockQty}" 
                                               style="width: 55px; padding: 4px; border: 1px solid var(--gray-300); border-radius: 4px;">
                                        <button type="submit" class="btn btn-outline btn-sm" style="padding: 4px 8px;">Update</button>
                                    </form>
                                </td>
                                <td style="font-weight: 700;">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00" /></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                        <input type="hidden" name="cartItemId" value="${item.id}">
                                        <button type="submit" class="btn btn-danger btn-sm" style="padding: 4px 8px;">✕</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Order Summary Card -->
            <div style="background: white; padding: 24px; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
                <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 20px;">Order Summary</h2>
                
                <div style="display: flex; justify-content: space-between; margin-bottom: 12px;">
                    <span>Items Total:</span>
                    <span>₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00" /></span>
                </div>
                <div style="display: flex; justify-content: space-between; margin-bottom: 12px;">
                    <span>Delivery:</span>
                    <span style="color: var(--success); font-weight: 600;">FREE</span>
                </div>
                <hr style="border: none; border-top: 1px solid var(--gray-200); margin: 16px 0;">
                <div style="display: flex; justify-content: space-between; margin-bottom: 24px; font-size: 1.2rem; font-weight: 800;">
                    <span>Running Total:</span>
                    <span style="color: var(--primary);">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00" /></span>
                </div>

                <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-block" style="padding: 12px;">
                    Proceed to Checkout ➔
                </a>

                <div style="margin-top: 16px;">
                    <form action="${pageContext.request.contextPath}/cart/clear" method="post">
                        <button type="submit" class="btn btn-outline btn-sm btn-block" onclick="return confirm('Clear entire cart?');">
                            Empty Cart
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
