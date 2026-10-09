<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<!-- Hero Banner -->
<div style="background: linear-gradient(135deg, #4f46e5 0%, #312e81 100%); color: white; padding: 40px; border-radius: 12px; margin-bottom: 30px;">
    <h1 style="font-size: 2.2rem; font-weight: 800; margin-bottom: 10px;">Welcome to RahimunishaMart</h1>
    <p style="font-size: 1.1rem; opacity: 0.9; max-width: 600px;">
        Explore curated electronics, fashion apparel, and lifestyle essentials. Fast delivery and secure transactions guaranteed.
    </p>
</div>

<!-- Category Filters -->
<div style="display: flex; gap: 10px; overflow-x: auto; padding-bottom: 10px; margin-bottom: 24px;">
    <a href="${pageContext.request.contextPath}/home" 
       class="btn ${empty currentCategory ? 'btn-primary' : 'btn-outline'} btn-sm">
        All Categories
    </a>
    <c:forEach var="cat" items="${categories}">
        <a href="${pageContext.request.contextPath}/home?category=<c:out value='${cat}' />" 
           class="btn ${currentCategory == cat ? 'btn-primary' : 'btn-outline'} btn-sm">
            <c:out value="${cat}" />
        </a>
    </c:forEach>
</div>

<!-- Product Results Summary -->
<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
    <h2 style="font-size: 1.3rem; font-weight: 700;">
        <c:choose>
            <c:when test="${not empty currentKeyword}">
                Search results for "<c:out value='${currentKeyword}' />" (${totalProducts})
            </c:when>
            <c:when test="${not empty currentCategory}">
                <c:out value="${currentCategory}" /> (${totalProducts})
            </c:when>
            <c:otherwise>
                Featured Products (${totalProducts})
            </c:otherwise>
        </c:choose>
    </h2>
</div>

<!-- Product Grid -->
<c:choose>
    <c:when test="${empty products}">
        <div style="text-align: center; padding: 60px 20px; background: white; border-radius: 10px;">
            <p style="font-size: 1.2rem; color: var(--gray-500); margin-bottom: 16px;">No products found matching your search.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Browse All Products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach var="p" items="${products}">
                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/products/${p.id}">
                        <img src="<c:out value='${p.imageUrl}' />" alt="<c:out value='${p.name}' />" class="product-img" onerror="this.src='https://via.placeholder.com/300x200?text=Product+Image'">
                    </a>
                    <div class="product-body">
                        <span class="product-category"><c:out value="${p.category}" /></span>
                        <h3 class="product-title">
                            <a href="${pageContext.request.contextPath}/products/${p.id}">
                                <c:out value="${p.name}" />
                            </a>
                        </h3>

                        <div style="font-size: 0.85rem; color: #f59e0b; margin-bottom: 6px;">
                            ★ <fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1" minFractionDigits="1" />
                            <span style="color: var(--gray-500);">(${p.reviewCount} reviews)</span>
                        </div>

                        <div class="product-price">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></div>

                        <div style="font-size: 0.8rem; margin-bottom: 12px; color: ${p.stockQty > 0 ? 'var(--success)' : 'var(--danger)'};">
                            <c:choose>
                                <c:when test="${p.stockQty > 0}">
                                    ● In Stock (${p.stockQty} units)
                                </c:when>
                                <c:otherwise>
                                    ● Out of Stock
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="product-actions">
                            <c:choose>
                                <c:when test="${p.stockQty > 0}">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="post" style="flex: 1;">
                                        <input type="hidden" name="productId" value="${p.id}">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn btn-primary btn-sm btn-block">Add to Cart</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-outline btn-sm btn-block" disabled style="opacity: 0.6;">Sold Out</button>
                                </c:otherwise>
                            </c:choose>

                            <c:if test="${empty sessionScope.currentUser || sessionScope.currentUser.role == 'BUYER'}">
                                <form action="${pageContext.request.contextPath}/wishlist/add" method="post">
                                    <input type="hidden" name="productId" value="${p.id}">
                                    <input type="hidden" name="redirect" value="${pageContext.request.requestURI}">
                                    <button type="submit" class="btn btn-outline btn-sm" title="Add to Wishlist">❤️</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <div style="display: flex; justify-content: center; gap: 8px; margin-top: 32px;">
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="${pageContext.request.contextPath}/home?page=${i}&category=<c:out value='${currentCategory}' />&q=<c:out value='${currentKeyword}' />"
                       class="btn ${currentPage == i ? 'btn-primary' : 'btn-outline'} btn-sm">
                        ${i}
                    </a>
                </c:forEach>
            </div>
        </c:if>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
