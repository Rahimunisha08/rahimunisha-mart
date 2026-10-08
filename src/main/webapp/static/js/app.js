document.addEventListener('DOMContentLoaded', () => {
    // Auto-update cart count badge if user logged in
    const cartBadge = document.getElementById('cartBadge');
    if (cartBadge) {
        const contextPath = window.APP_CONTEXT_PATH || '';
        fetch(contextPath + '/cart/count')
            .then(res => res.json())
            .then(data => {
                if (data && typeof data.count === 'number') {
                    cartBadge.textContent = data.count;
                    cartBadge.style.display = data.count > 0 ? 'inline-block' : 'none';
                }
            })
            .catch(() => {});
    }

    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});
