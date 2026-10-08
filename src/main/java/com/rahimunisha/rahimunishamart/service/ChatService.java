package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.ai.ChatProvider;
import com.rahimunisha.rahimunishamart.ai.ChatProviderFactory;
import com.rahimunisha.rahimunishamart.dto.ChatResponseDTO;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static final int MAX_INPUT_LENGTH = 500;
    private static final int RATE_LIMIT_MESSAGES_PER_MINUTE = 10;
    private static final long RATE_LIMIT_WINDOW_MS = 60 * 1000L;

    private static final String SESSION_CACHE_ATTR = "CHATBOT_CACHE";
    private static final String SESSION_TIMESTAMPS_ATTR = "CHATBOT_TIMESTAMPS";

    private final ChatProvider chatProvider;

    public ChatService() {
        this.chatProvider = ChatProviderFactory.getProvider();
    }

    public ChatService(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
    }

    public ChatResponseDTO processChat(String message, HttpSession session) {
        // 1. Validate Input
        if (!ValidationUtil.isNonEmpty(message)) {
            throw new ValidationException("message", "Chat query cannot be empty.");
        }

        String trimmed = message.trim();
        if (trimmed.length() > MAX_INPUT_LENGTH) {
            throw new ValidationException("message", "Message exceeds maximum allowed length of " + MAX_INPUT_LENGTH + " characters.");
        }

        // 2. Enforce Per-Session Rate Limit (10 messages per minute)
        enforceRateLimit(session);

        // 3. In-memory Per-Session Question Caching
        String normalizedKey = trimmed.toLowerCase();
        Map<String, String> sessionCache = getSessionCache(session);
        if (sessionCache.containsKey(normalizedKey)) {
            logger.info("Serving chatbot response from session cache for query: '{}'", trimmed);
            return new ChatResponseDTO(sessionCache.get(normalizedKey), true, chatProvider.getProviderName());
        }

        // 4. Invoke Provider with Fail-Safe Try/Catch & Static Degraded Fallback
        String reply;
        try {
            reply = chatProvider.generateReply(trimmed);
        } catch (Exception e) {
            logger.error("ChatProvider exception occurred during query processing: {}", e.getMessage(), e);
            // Degraded static fallback per Week 9 requirement
            reply = "I apologize, our smart assistant is currently experiencing high load. " +
                    "For order or returns assistance, please check 'My Orders' or reach out to support@rahimunishamart.com.";
        }

        // Cache the successful/fallback reply in the session
        sessionCache.put(normalizedKey, reply);

        return new ChatResponseDTO(reply, false, chatProvider.getProviderName());
    }

    @SuppressWarnings("unchecked")
    private void enforceRateLimit(HttpSession session) {
        synchronized (session) {
            long now = System.currentTimeMillis();
            Deque<Long> timestamps = (Deque<Long>) session.getAttribute(SESSION_TIMESTAMPS_ATTR);
            if (timestamps == null) {
                timestamps = new ArrayDeque<>();
                session.setAttribute(SESSION_TIMESTAMPS_ATTR, timestamps);
            }

            // Remove timestamps older than 60 seconds
            while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > RATE_LIMIT_WINDOW_MS) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= RATE_LIMIT_MESSAGES_PER_MINUTE) {
                throw new ValidationException("rate_limit", "Rate limit exceeded (maximum 10 messages/minute). Please wait a moment before sending another message.");
            }

            timestamps.addLast(now);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSessionCache(HttpSession session) {
        synchronized (session) {
            Map<String, String> cache = (Map<String, String>) session.getAttribute(SESSION_CACHE_ATTR);
            if (cache == null) {
                cache = new HashMap<>();
                session.setAttribute(SESSION_CACHE_ATTR, cache);
            }
            return cache;
        }
    }
}
