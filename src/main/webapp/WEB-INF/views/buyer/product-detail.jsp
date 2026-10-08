<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<c:if test="${param.reviewSuccess == 'true'}">
    <div class="alert alert-success">Thank you! Your verified review has been submitted.</div>
</c:if>
<c:if test="${not empty param.reviewError}">
    <div class="alert alert-error"><c:out value="${param.reviewError}" /></div>
</c:if>

<div style="display: grid; grid-template-columns: 1fr 1fr; gap: 40px; background: white; padding: 32px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); margin-bottom: 40px;">
    <!-- Product Image -->
    <div>
        <img src="<c:out value='${product.imageUrl}' />" alt="<c:out value='${product.name}' />" 
             style="width: 100%; max-height: 420px; object-fit: cover; border-radius: 8px; border: 1px solid var(--gray-200);"
             onerror="this.src='https://via.placeholder.com/500x400?text=Product+Image'">
    </div>

    <!-- Product Details -->
    <div>
        <span class="product-category"><c:out value="${product.category}" /></span>
        <h1 style="font-size: 1.8rem; font-weight: 800; color: var(--dark); margin: 8px 0;"><c:out value="${product.name}" /></h1>
        
        <p style="color: var(--gray-500); font-size: 0.9rem; margin-bottom: 12px;">
            Sold by <strong><c:out value="${product.sellerName}" /></strong>
        </p>

        <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 16px;">
            <span style="font-size: 1.1rem; color: #f59e0b; font-weight: 700;">
                ★ <fmt:formatNumber value="${product.averageRating}" maxFractionDigits="1" minFractionDigits="1" />
            </span>
            <span style="color: var(--gray-500); font-size: 0.9rem;">
                (${product.reviewCount} customer reviews)
            </span>
        </div>

        <div class="product-price" style="font-size: 2rem; margin-bottom: 16px;">
            ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00" />
        </div>

        <div style="margin-bottom: 20px; color: ${product.stockQty > 0 ? 'var(--success)' : 'var(--danger)'}; font-weight: 600;">
            <c:choose>
                <c:when test="${product.stockQty > 0}">
                    ● In Stock (${product.stockQty} available)
                </c:when>
                <c:otherwise>
                    ● Currently Out of Stock
                </c:otherwise>
            </c:choose>
        </div>

        <p style="color: var(--gray-700); margin-bottom: 24px; line-height: 1.6;">
            <c:out value="${product.description}" />
        </p>

        <c:choose>
            <c:when test="${product.stockQty > 0}">
                <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display: flex; gap: 12px; align-items: center; margin-bottom: 16px;">
                    <input type="hidden" name="productId" value="${product.id}">
                    <label for="quantity" style="font-weight: 600;">Qty:</label>
                    <input type="number" id="quantity" name="quantity" value="1" min="1" max="${product.stockQty}" 
                           style="width: 70px; padding: 8px; border: 1px solid var(--gray-300); border-radius: 6px;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Add to Cart</button>
                </form>
            </c:when>
            <c:otherwise>
                <button class="btn btn-outline" disabled style="width: 100%; opacity: 0.6; margin-bottom: 16px;">Out of Stock</button>
            </c:otherwise>
        </c:choose>

        <c:if test="${sessionScope.currentUser.role == 'BUYER'}">
            <form action="${pageContext.request.contextPath}/wishlist/add" method="post">
                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="redirect" value="${pageContext.request.requestURI}">
                <button type="submit" class="btn btn-outline btn-block">❤️ Add to Wishlist</button>
            </form>
        </c:if>
    </div>
</div>

<!-- Customer Reviews Section (F8) -->
<div style="background: white; padding: 32px; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
    <h2 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 20px;">Customer Reviews & Ratings</h2>

    <!-- Verified Review Submission Box -->
    <c:choose>
        <c:when test="${canReview}">
            <div style="background: #f8fafc; border: 1px solid var(--gray-200); padding: 20px; border-radius: 8px; margin-bottom: 24px;">
                <h3 style="font-size: 1.1rem; font-weight: 600; margin-bottom: 12px;">Leave a Verified Purchase Review</h3>
                <form action="${pageContext.request.contextPath}/reviews/add" method="post">
                    <input type="hidden" name="productId" value="${product.id}">
                    
                    <div class="form-group">
                        <label class="form-label">Star Rating (1 to 5):</label>
                        <select name="rating" class="form-control" style="max-width: 150px;" required>
                            <option value="5">★★★★★ (5 Stars)</option>
                            <option value="4">★★★★☆ (4 Stars)</option>
                            <option value="3">★★★☆☆ (3 Stars)</option>
                            <option value="2">★★☆☆☆ (2 Stars)</option>
                            <option value="1">★☆☆☆☆ (1 Star)</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Your Review:</label>
                        <textarea name="comment" rows="3" class="form-control" placeholder="Share your experience with this item..." required></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary btn-sm">Submit Review</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div style="background: var(--gray-100); padding: 12px 16px; border-radius: 6px; font-size: 0.85rem; color: var(--gray-500); margin-bottom: 20px;">
                ℹ️ Reviews can only be submitted by verified customers who have completed an order for this product.
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Reviews List -->
    <c:choose>
        <c:when test="${empty reviews}">
            <p style="color: var(--gray-500);">No reviews yet. Be the first to try this item!</p>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 16px;">
                <c:forEach var="r" items="${reviews}">
                    <div style="border-bottom: 1px solid var(--gray-200); padding-bottom: 16px;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
                            <strong><c:out value="${r.userName}" /></strong>
                            <span style="font-size: 0.8rem; color: var(--gray-500);">
                                <fmt:formatDate value="${r.createdAt}" pattern="MMM dd, yyyy" />
                            </span>
                        </div>
                        <div style="color: #f59e0b; font-size: 0.9rem; margin-bottom: 6px;">
                            <c:forEach begin="1" end="${r.rating}">★</c:forEach>
                            <c:forEach begin="${r.rating + 1}" end="5">☆</c:forEach>
                        </div>
                        <p style="color: var(--gray-700); font-size: 0.95rem;">
                            <c:out value="${r.comment}" />
                        </p>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
