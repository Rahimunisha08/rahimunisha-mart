<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 24px;">
    <h1 style="font-size: 1.8rem; font-weight: 800;">Administrator Governance Portal</h1>
    <p style="color: var(--gray-500);">Full oversight across platform users, order settlements, and product catalog moderation.</p>
</div>

<c:if test="${not empty param.msg}">
    <div class="alert alert-success"><c:out value="${param.msg}" /></div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="alert alert-error"><c:out value="${param.error}" /></div>
</c:if>

<!-- Metrics Overview -->
<div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; margin-bottom: 30px;">
    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid var(--primary);">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600;">Registered Users</div>
        <div style="font-size: 1.8rem; font-weight: 800; margin-top: 6px;">${users.size()}</div>
    </div>
    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid var(--secondary);">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600;">Platform Listings</div>
        <div style="font-size: 1.8rem; font-weight: 800; margin-top: 6px;">${products.size()}</div>
    </div>
    <div style="background: white; padding: 20px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); border-left: 4px solid var(--success);">
        <div style="font-size: 0.85rem; color: var(--gray-500); font-weight: 600;">Total Orders Placed</div>
        <div style="font-size: 1.8rem; font-weight: 800; margin-top: 6px;">${orders.size()}</div>
    </div>
</div>

<!-- Section 1: Product Moderation (F7) -->
<div style="background: white; padding: 24px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); margin-bottom: 30px;">
    <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 16px;">Product Catalog Moderation</h2>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Seller</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Moderate Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>
                            <a href="${pageContext.request.contextPath}/products/${p.id}" target="_blank" style="font-weight: 600;">
                                <c:out value="${p.name}" />
                            </a>
                        </td>
                        <td><c:out value="${p.sellerName}" /></td>
                        <td><c:out value="${p.category}" /></td>
                        <td>₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></td>
                        <td><span class="status-badge status-${p.status}"><c:out value="${p.status}" /></span></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/products/status" method="post" style="display: inline;">
                                <input type="hidden" name="productId" value="${p.id}">
                                <c:choose>
                                    <c:when test="${p.status == 'ACTIVE'}">
                                        <input type="hidden" name="status" value="BLOCKED">
                                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Block this listing?');">Block</button>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="status" value="ACTIVE">
                                        <button type="submit" class="btn btn-success btn-sm">Unblock</button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Section 2: Platform Users (F7) -->
<div style="background: white; padding: 24px; border-radius: var(--border-radius); box-shadow: var(--card-shadow); margin-bottom: 30px;">
    <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 16px;">Platform User Directory</h2>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Phone</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>#${u.id}</td>
                        <td style="font-weight: 600;"><c:out value="${u.fullName}" /></td>
                        <td><c:out value="${u.email}" /></td>
                        <td><span class="badge" style="background: ${u.role == 'ADMIN' ? 'var(--danger)' : u.role == 'SELLER' ? 'var(--warning)' : 'var(--primary)'};"><c:out value="${u.role}" /></span></td>
                        <td><c:out value="${u.phone != null ? u.phone : '—'}" /></td>
                        <td>
                            <c:if test="${u.role != 'ADMIN'}">
                                <form action="${pageContext.request.contextPath}/admin/users/delete" method="post" style="display: inline;">
                                    <input type="hidden" name="userId" value="${u.id}">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Delete user #${u.id}?');">Delete</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Section 3: Orders Audit (F7) -->
<div style="background: white; padding: 24px; border-radius: var(--border-radius); box-shadow: var(--card-shadow);">
    <h2 style="font-size: 1.3rem; font-weight: 700; margin-bottom: 16px;">Orders Audit Log</h2>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>Order</th>
                    <th>Customer</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Payment</th>
                    <th>Placed At</th>
                    <th>Inspect</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td style="font-weight: 700;">#${o.id}</td>
                        <td><c:out value="${o.buyerName}" /></td>
                        <td style="font-weight: 700;">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00" /></td>
                        <td><span class="status-badge status-${o.status}"><c:out value="${o.status}" /></span></td>
                        <td><c:out value="${o.paymentMethod}" /></td>
                        <td><fmt:formatDate value="${o.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/orders/${o.id}" class="btn btn-outline btn-sm">Audit</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
