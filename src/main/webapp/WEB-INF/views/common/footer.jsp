<%@ page contentType="text/html;charset=UTF-8" language="java" %>
</main>

<footer class="footer">
    <div class="container">
        <p>&copy; 2026 <strong>RahimunishaMart</strong>. Anna University R2025 Semester 3 Capstone Project.</p>
        <p style="margin-top: 6px; font-size: 0.8rem; color: #94a3b8;">
            Java Servlets 4.0 · Apache Tomcat 9.0 · HikariCP · H2 Database · AI Chatbot Proxy
        </p>
    </div>
</footer>

<!-- Include AI Chatbot Widget -->
<jsp:include page="/WEB-INF/views/common/chatbot.jsp" />

<script src="${pageContext.request.contextPath}/static/js/app.js"></script>
<script src="${pageContext.request.contextPath}/static/js/chatbot.js"></script>
</body>
</html>
