<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="form-card" style="max-width: 650px;">
    <h1 style="font-size: 1.6rem; font-weight: 800; margin-bottom: 8px;">
        <c:choose>
            <c:when test="${not empty product}">Edit Product Listing</c:when>
            <c:otherwise>Create New Product Listing</c:otherwise>
        </c:choose>
    </h1>
    <p style="color: var(--gray-500); font-size: 0.9rem; margin-bottom: 24px;">
        Provide detailed product specs, pricing, and initial inventory stock.
    </p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error"><c:out value="${errorMessage}" /></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/seller/products/<c:out value='${not empty product ? "edit" : "new"}' />" method="post">
        <c:if test="${not empty product}">
            <input type="hidden" name="id" value="${product.id}">
        </c:if>

        <div class="form-group">
            <label class="form-label" for="name">Product Title:</label>
            <input type="text" id="name" name="name" class="form-control" 
                   value="<c:out value='${product != null ? product.name : param.name}' />" 
                   placeholder="e.g. Sony WH-1000XM5 Wireless Headphones" required>
            <c:if test="${not empty fieldErrors.name}">
                <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.name}" /></span>
            </c:if>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
                <label class="form-label" for="category">Category:</label>
                <select id="category" name="category" class="form-control" required>
                    <option value="Electronics" ${product.category == 'Electronics' ? 'selected' : ''}>Electronics</option>
                    <option value="Fashion" ${product.category == 'Fashion' ? 'selected' : ''}>Fashion</option>
                    <option value="Home & Living" ${product.category == 'Home & Living' ? 'selected' : ''}>Home & Living</option>
                    <option value="Groceries" ${product.category == 'Groceries' ? 'selected' : ''}>Groceries</option>
                    <option value="Sports & Fitness" ${product.category == 'Sports & Fitness' ? 'selected' : ''}>Sports & Fitness</option>
                    <option value="Books & Stationery" ${product.category == 'Books & Stationery' ? 'selected' : ''}>Books & Stationery</option>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label" for="price">Price (₹ INR):</label>
                <input type="number" id="price" name="price" step="0.01" min="0.01" class="form-control" 
                       value="<c:out value='${product != null ? product.price : param.price}' />" 
                       placeholder="2499.00" required>
                <c:if test="${not empty fieldErrors.price}">
                    <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.price}" /></span>
                </c:if>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
            <div class="form-group">
                <label class="form-label" for="stockQty">Available Inventory Stock:</label>
                <input type="number" id="stockQty" name="stockQty" min="0" class="form-control" 
                       value="<c:out value='${product != null ? product.stockQty : param.stockQty != null ? param.stockQty : "20"}' />" 
                       required>
                <c:if test="${not empty fieldErrors.stockQty}">
                    <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.stockQty}" /></span>
                </c:if>
            </div>

            <c:if test="${not empty product}">
                <div class="form-group">
                    <label class="form-label" for="status">Listing Visibility:</label>
                    <select id="status" name="status" class="form-control">
                        <option value="ACTIVE" ${product.status == 'ACTIVE' ? 'selected' : ''}>Active (Available in Store)</option>
                        <option value="INACTIVE" ${product.status == 'INACTIVE' ? 'selected' : ''}>Inactive (Hidden)</option>
                    </select>
                </div>
            </c:if>
        </div>

        <div class="form-group">
            <label class="form-label" for="imageUrl">Image URL (Public HTTPS link):</label>
            <input type="url" id="imageUrl" name="imageUrl" class="form-control" 
                   value="<c:out value='${product != null ? product.imageUrl : param.imageUrl}' />" 
                   placeholder="https://images.unsplash.com/photo-..." required>
        </div>

        <div class="form-group">
            <label class="form-label" for="description">Detailed Description:</label>
            <textarea id="description" name="description" rows="4" class="form-control" 
                      placeholder="Features, technical specs, dimensions, and warranty coverage..."><c:out value="${product != null ? product.description : param.description}" /></textarea>
        </div>

        <div style="display: flex; justify-content: space-between; margin-top: 24px;">
            <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-outline">Cancel</a>
            <button type="submit" class="btn btn-primary">
                <c:out value="${not empty product ? 'Save Changes' : 'Publish Listing'}" />
            </button>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
