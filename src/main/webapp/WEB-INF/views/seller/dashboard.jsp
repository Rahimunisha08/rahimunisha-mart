<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
    <div>
        <h1 style="font-size: 1.8rem; font-weight: 800;">Seller Command Center</h1>
        <p style="color: var(--gray-500);">Manage product listings, inventory levels, and monitor customer orders.</p>
    </div>
    <div style="display: flex; gap: 10px;">
        <a href="${pageContext.request.contextPath}/seller/orders" class="btn btn-outline">📦 View Orders</a>
        <a href="${pageContext.request.contextPath}/seller/products/new" class="btn btn-primary">➕ Add New Listing</a>
    </div>
</div>

<c:if test="${param.success != null}">
    <div class="alert alert-success"><c:out value="${param.success}" /></div>
</c:if>
<c:if test="${param.deleted == 'true'}">
    <div class="alert alert-success">Product removed from catalog successfully.</div>
</c:if>
<c:if test="${param.error != null}">
    <div class="alert alert-error"><c:out value="${param.error}" /></div>
</c:if>

<!-- Sales Metrics Cards (O3 Seller Sales Dashboard) -->
<div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; margin-bottom: 30px;">
    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid var(--primary);">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600; text-transform: uppercase;">Total Sales Revenue</div>
        <div style="font-size: 1.8rem; font-weight: 800; color: var(--dark); margin-top: 6px;">
            ₹<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00" />
        </div>
    </div>

    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid var(--success);">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600; text-transform: uppercase;">Active Listings</div>
        <div style="font-size: 1.8rem; font-weight: 800; color: var(--dark); margin-top: 6px;">
            ${products.size()} Products
        </div>
    </div>

    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid var(--secondary);">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600; text-transform: uppercase;">Total Units Sold</div>
        <div style="font-size: 1.8rem; font-weight: 800; color: var(--dark); margin-top: 6px;">
            ${totalItemsSold} Items
        </div>
    </div>

    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid ${lowStockCount > 0 ? 'var(--danger)' : 'var(--success)'};">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600; text-transform: uppercase;">Low Stock Warning</div>
        <div style="font-size: 1.8rem; font-weight: 800; color: ${lowStockCount > 0 ? 'var(--danger)' : 'var(--dark)'}; margin-top: 6px;">
            ${lowStockCount} Products (&le;5)
        </div>
    </div>
</div>

<!-- Seller's Listings Table (F2 & Week 3 Deliverable) -->
<div style="background: white; padding: 24px; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
    <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 16px;">Product Listings Catalog</h2>

    <c:choose>
        <c:when test="${empty products}">
            <p style="color: var(--gray-500); text-align: center; padding: 40px 0;">No active listings yet. Click 'Add New Listing' to create one!</p>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Rating</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 12px;">
                                        <img src="<c:out value='${p.imageUrl}' />" alt="<c:out value='${p.name}' />" 
                                             style="width: 44px; height: 44px; object-fit: cover; border-radius: 6px;"
                                             onerror="this.src='https://via.placeholder.com/44'">
                                        <a href="${pageContext.request.contextPath}/products/${p.id}" style="font-weight: 600;">
                                            <c:out value="${p.name}" />
                                        </a>
                                    </div>
                                </td>
                                <td><c:out value="${p.category}" /></td>
                                <td style="font-weight: 700;">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></td>
                                <td>
                                    <span style="font-weight: 700; color: ${p.stockQty <= 5 ? 'var(--danger)' : 'inherit'};">
                                        ${p.stockQty}
                                    </span>
                                    <c:if test="${p.stockQty <= 5}">
                                        <small style="color: var(--danger); font-size: 0.75rem;">(Low!)</small>
                                    </c:if>
                                </td>
                                <td>
                                    ★ <fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1" minFractionDigits="1" />
                                    <small>(${p.reviewCount})</small>
                                </td>
                                <td><span class="status-badge status-${p.status}"><c:out value="${p.status}" /></span></td>
                                <td>
                                    <div style="display: flex; gap: 6px;">
                                        <a href="${pageContext.request.contextPath}/seller/products/edit?id=${p.id}" class="btn btn-outline btn-sm">Edit</a>
                                        <form action="${pageContext.request.contextPath}/seller/products/delete" method="post" style="display: inline;">
                                            <input type="hidden" name="id" value="${p.id}">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Delete this listing permanently?');">Delete</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
