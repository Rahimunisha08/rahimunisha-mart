<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Floating AI Chatbot Widget (Week 10 / O4 Deliverable) -->
<button id="chatLauncher" class="chat-launcher-btn" title="Chat with RahimunishaMart Assistant" aria-label="Open AI Assistant">
    💬
</button>

<div id="chatPanel" class="chat-panel" role="dialog" aria-labelledby="chatTitle">
    <div class="chat-header">
        <div>
            <div id="chatTitle" class="chat-header-title">🤖 RahimunishaMart Assistant</div>
            <div class="chat-header-status">Online · Instant Answers</div>
        </div>
        <button id="chatCloseBtn" class="chat-close-btn" aria-label="Close Chat">&times;</button>
    </div>

    <div id="chatMessages" class="chat-messages">
        <div class="chat-msg chat-msg-bot">
            Hello! I am your RahimunishaMart shopping assistant. How may I assist you with orders, returns, payments, or product details today?
        </div>
    </div>

    <!-- Quick FAQ Chips per Week 10 requirement -->
    <div class="chat-quick-chips">
        <button type="button" class="chat-chip" data-question="How do I track my order?">Track Order</button>
        <button type="button" class="chat-chip" data-question="What is the return policy?">Returns</button>
        <button type="button" class="chat-chip" data-question="What payment methods do you support?">Payments</button>
        <button type="button" class="chat-chip" data-question="How long does shipping take?">Shipping Time</button>
        <button type="button" class="chat-chip" data-question="How can I become a seller?">Seller Onboarding</button>
    </div>

    <div class="chat-input-area">
        <input type="text" id="chatInput" class="chat-input" placeholder="Ask about products, orders, returns..." maxlength="500">
        <button type="button" id="chatSendBtn" class="chat-send-btn" aria-label="Send">➤</button>
    </div>
</div>
