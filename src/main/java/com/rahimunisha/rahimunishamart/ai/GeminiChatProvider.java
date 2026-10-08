package com.rahimunisha.rahimunishamart.ai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rahimunisha.rahimunishamart.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * GeminiChatProvider: Connects to Google Gemini API (gemini-1.5-flash or gemini-2.0-flash).
 * Uses server-side API key only (via env GEMINI_API_KEY or config.properties).
 * Fixed system prompt template limits scope strictly to e-commerce assistance.
 */
public class GeminiChatProvider implements ChatProvider {
    private static final Logger logger = LoggerFactory.getLogger(GeminiChatProvider.class);

    private static final String SYSTEM_PROMPT =
            "You are the intelligent customer service AI assistant for RahimunishaMart, an e-commerce platform. " +
            "Your domain is strictly limited to helping customers with products, listings, orders, payments, shipping, returns, and seller inquiries. " +
            "If a user asks about unrelated topics (such as politics, math puzzles, coding general software, etc.), politely decline and guide them back to RahimunishaMart shopping. " +
            "Keep your answers concise, warm, professional, and helpful (under 3 paragraphs).";

    private final String apiKey;
    private final HttpClient httpClient;

    public GeminiChatProvider() {
        this.apiKey = ConfigUtil.get("gemini.api.key", ConfigUtil.get("GEMINI_API_KEY", ""));
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(4))
                .build();
    }

    public GeminiChatProvider(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(4))
                .build();
    }

    @Override
    public String generateReply(String userMessage) throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.contains("your_gemini_api_key")) {
            throw new IllegalStateException("Gemini API key is not configured in server environment.");
        }

        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        // Build Gemini JSON payload
        JsonObject payload = new JsonObject();
        JsonArray contents = new JsonArray();

        JsonObject userContent = new JsonObject();
        userContent.addProperty("role", "user");
        JsonArray parts = new JsonArray();
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", SYSTEM_PROMPT + "\n\nCustomer question: " + userMessage);
        parts.add(textPart);
        userContent.add("parts", parts);
        contents.add(userContent);

        payload.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(6)) // Outbound API timeout per requirement
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray candidates = responseJson.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject content = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                JsonArray resParts = content.getAsJsonArray("parts");
                if (resParts != null && resParts.size() > 0) {
                    return resParts.get(0).getAsJsonObject().get("text").getAsString().trim();
                }
            }
        }

        logger.warn("Gemini API returned status code: {} with body: {}", response.statusCode(), response.body());
        throw new RuntimeException("Gemini API call failed with status: " + response.statusCode());
    }

    @Override
    public String getProviderName() {
        return "GeminiChatProvider (Google Gemini LLM)";
    }
}
