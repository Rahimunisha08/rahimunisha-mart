package com.rahimunisha.rahimunishamart.ai;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * MockChatProvider: Canned domain-specific responses with zero network dependency.
 * Accurately answers 10+ FAQ e-commerce domain questions.
 */
public class MockChatProvider implements ChatProvider {

    private final Map<String, String> faqDatabase = new HashMap<>();

    public MockChatProvider() {
        faqDatabase.put("shipping", "Standard shipping across Tamil Nadu takes 1–2 business days. Pan-India deliveries arrive within 3–5 business days. Free shipping applies to orders above ₹999!");
        faqDatabase.put("delivery", "Orders are delivered via trusted courier partners. You can monitor the real-time shipping status in your 'My Orders' section.");
        faqDatabase.put("return", "We provide a 7-day hassle-free return policy on eligible electronics, fashion, and lifestyle items if unused and in original packaging.");
        faqDatabase.put("refund", "Once the seller inspects the returned item, refunds are credited back to your original payment method within 3 to 5 working days.");
        faqDatabase.put("payment", "RahimunishaMart accepts Mock Credit/Debit Cards, UPI (GPay, PhonePe, Paytm), Net Banking, and Cash on Delivery (COD).");
        faqDatabase.put("track", "To track your order, log in to your account, click 'My Orders' in the top menu, and check the status badge (Pending, Confirmed, Shipped, or Delivered).");
        faqDatabase.put("cancel", "You can cancel any order directly from the Order History page while its status is still 'Pending' or 'Confirmed' before shipment.");
        faqDatabase.put("seller", "To become a seller on RahimunishaMart, choose the 'Seller' role upon registration. You will gain instant access to the Seller Dashboard to list products and fulfill orders.");
        faqDatabase.put("warranty", "All electronics listings carry the official brand warranty (minimum 1 year). Invoices can be downloaded from your Order History.");
        faqDatabase.put("contact", "You can reach our 24/7 customer support team at support@rahimunishamart.com or call our helpline at +91 98765 43210.");
        faqDatabase.put("discount", "Enjoy up to 20% festive discount on selected electronics and fashion items this week!");
    }

    @Override
    public String generateReply(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! I am RahimunishaMart AI Assistant. How can I help you with your shopping today?";
        }

        String lower = userMessage.toLowerCase(Locale.ROOT);

        for (Map.Entry<String, String> entry : faqDatabase.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey")) {
            return "Greetings! Welcome to RahimunishaMart. I can assist you with tracking orders, payment methods, returns, seller inquiries, and product questions. What would you like to know?";
        }

        if (lower.contains("help") || lower.contains("faq")) {
            return "I can answer questions regarding: 1) Order Shipping & Delivery, 2) Returns & Refunds, 3) Payment Methods, 4) Order Tracking, 5) Seller Onboarding, and 6) Warranty info.";
        }

        // Domain-restricted fallback
        return "Thank you for asking about RahimunishaMart! For this query, our customer care specialists are also available at support@rahimunishamart.com. You can also explore our catalog by category above.";
    }

    @Override
    public String getProviderName() {
        return "MockChatProvider (Offline FAQ)";
    }
}
