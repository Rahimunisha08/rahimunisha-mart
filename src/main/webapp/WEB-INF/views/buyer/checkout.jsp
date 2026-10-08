<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 24px;">Checkout & Mock Payment</h1>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
</c:if>

<div style="display: grid; grid-template-columns: 3fr 2fr; gap: 30px;">
    <!-- Shipping Address & Payment Selection Form -->
    <div style="background: white; padding: 28px; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 18px;">1. Shipping Information</h2>
        
        <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">
            <div class="form-group">
                <label class="form-label" for="shippingAddress">Delivery Street Address:</label>
                <textarea id="shippingAddress" name="shippingAddress" rows="3" class="form-control" placeholder="House/Flat No., Street, City, Pincode" required><c:out value="${userAddress != null ? userAddress : sessionScope.currentUser.address}" /></textarea>
                <c:if test="${not empty fieldErrors.shippingAddress}">
                    <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.shippingAddress}" /></span>
                </c:if>
            </div>

            <h2 style="font-size: 1.25rem; font-weight: 700; margin: 24px 0 16px;">2. Mock Payment Method (Simulation)</h2>
            <div style="background: #f8fafc; border: 1px solid var(--gray-200); padding: 16px; border-radius: 8px; margin-bottom: 20px;">
                <p style="font-size: 0.85rem; color: var(--gray-500); margin-bottom: 12px;">
                    🛡️ This is a sandbox capstone mock checkout. No real money or credit card is charged.
                </p>

                <div style="display: flex; flex-direction: column; gap: 10px;">
                    <label style="display: flex; align-items: center; gap: 8px; font-weight: 500; cursor: pointer;">
                        <input type="radio" name="paymentMethod" value="MOCK_CARD" checked>
                        <span>💳 Mock Credit / Debit Card (Instant Approval)</span>
                    </label>
                    <label style="display: flex; align-items: center; gap: 8px; font-weight: 500; cursor: pointer;">
                        <input type="radio" name="paymentMethod" value="MOCK_UPI">
                        <span>📱 Mock UPI / QR (PhonePe / GPay Simulation)</span>
                    </label>
                    <label style="display: flex; align-items: center; gap: 8px; font-weight: 500; cursor: pointer;">
                        <input type="radio" name="paymentMethod" value="MOCK_COD">
                        <span>💵 Cash on Delivery (COD)</span>
                    </label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="padding: 14px; font-size: 1.05rem;">
                Confirm & Place Order (₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00" />)
            </button>
        </form>
    </div>

    <!-- Review Items in Checkout -->
    <div style="background: white; padding: 24px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); height: fit-content;">
        <h2 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 16px;">Order Summary (${cartItems.size()} items)</h2>
        <div style="display: flex; flex-direction: column; gap: 12px; margin-bottom: 20px;">
            <c:forEach var="item" items="${cartItems}">
                <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--gray-100); padding-bottom: 10px;">
                    <div>
                        <div style="font-weight: 600; font-size: 0.9rem;"><c:out value="${item.product.name}" /></div>
                        <div style="font-size: 0.8rem; color: var(--gray-500);">Qty: ${item.quantity} × ₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00" /></div>
                    </div>
                    <div style="font-weight: 700;">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00" /></div>
                </div>
            </c:forEach>
        </div>

        <div style="display: flex; justify-content: space-between; font-size: 1.2rem; font-weight: 800; border-top: 2px solid var(--gray-200); padding-top: 14px;">
            <span>Total Payable:</span>
            <span style="color: var(--primary);">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00" /></span>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
