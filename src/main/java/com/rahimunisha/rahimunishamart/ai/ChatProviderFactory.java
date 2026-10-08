package com.rahimunisha.rahimunishamart.ai;

import com.rahimunisha.rahimunishamart.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChatProviderFactory: Factory pattern implementation to instantiate the appropriate ChatProvider.
 */
public class ChatProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger(ChatProviderFactory.class);

    private ChatProviderFactory() {
    }

    public static ChatProvider getProvider() {
        String providerType = ConfigUtil.get("ai.chatbot.provider", "mock").trim().toLowerCase();

        if ("gemini".equals(providerType)) {
            String apiKey = ConfigUtil.get("gemini.api.key", ConfigUtil.get("GEMINI_API_KEY", ""));
            if (apiKey != null && !apiKey.isEmpty() && !apiKey.contains("your_gemini_api_key")) {
                logger.info("Initializing GeminiChatProvider as active AI engine.");
                return new GeminiChatProvider(apiKey);
            } else {
                logger.warn("ai.chatbot.provider is set to 'gemini' but GEMINI_API_KEY is missing. Falling back to MockChatProvider.");
            }
        }

        logger.info("Initializing MockChatProvider as active AI engine.");
        return new MockChatProvider();
    }
}
