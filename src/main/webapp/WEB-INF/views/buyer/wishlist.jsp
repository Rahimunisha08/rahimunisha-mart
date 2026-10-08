<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<h1 style="font-size: 1.8rem; font-weight: 800; margin-bottom: 24px;">Your Saved Wishlist</h1>

<c:choose>
    <c:when test="${empty wishlistItems}">
        <div style="background: white; padding: 60px 20px; text-align: center; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
            <div style="font-size: 3rem; margin-bottom: 12px;">❤️</div>
            <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 8px;">Your wishlist is currently empty</h2>
            <p style="color: var(--gray-500); margin-bottom: 20px;">Save items you love so you can easily purchase them later!</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Discover Products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach var="item" items="${wishlistItems}">
                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/products/${item.product.id}">
                        <img src="<c:out value='${item.product.imageUrl}' />" alt="<c:out value='${item.product.name}' />" class="product-img"
                             onerror="this.src='https://via.placeholder.com/300x200?text=Product'">
                    </a>
                    <div class="product-body">
                        <span class="product-category"><c:out value="${item.product.category}" /></span>
                        <h3 class="product-title">
                            <a href="${pageContext.request.contextPath}/products/${item.product.id}">
                                <c:out value="${item.product.name}" />
                            </a>
                        </h3>
                        <div class="product-price">₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00" /></div>

                        <div class="product-actions" style="margin-top: 16px;">
                            <c:if test="${item.product.stockQty > 0}">
                                <form action="${pageContext.request.contextPath}/cart/add" method="post" style="flex: 1;">
                                    <input type="hidden" name="productId" value="${item.product.id}">
                                    <input type="hidden" name="quantity" value="1">
                                    <button type="submit" class="btn btn-primary btn-sm btn-block">Move to Cart</button>
                                </form>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/wishlist/remove" method="post">
                                <input type="hidden" name="productId" value="${item.product.id}">
                                <button type="submit" class="btn btn-danger btn-sm" title="Remove">✕</button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
