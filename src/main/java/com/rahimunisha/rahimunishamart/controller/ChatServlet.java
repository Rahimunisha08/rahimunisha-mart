package com.rahimunisha.rahimunishamart.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rahimunisha.rahimunishamart.dto.ApiResponse;
import com.rahimunisha.rahimunishamart.dto.ChatResponseDTO;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.service.ChatService;
import com.rahimunisha.rahimunishamart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * ChatServlet: AI Chatbot Gateway Endpoint.
 * Supports /api/chat and /api/v1/chat
 * Features:
 * - Rate limiting (10 msg/min per session)
 * - In-memory session cache
 * - Input validation & 500-character cap
 * - Response envelope: { success, data: { reply, cached, provider }, error }
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/api/chat", "/api/v1/chat"})
public class ChatServlet extends HttpServlet {
    private final ChatService chatService = new ChatService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(true);
        String message = extractMessage(req);

        try {
            ChatResponseDTO responseDTO = chatService.processChat(message, session);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.success(responseDTO)));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error(e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(JsonUtil.toJson(ApiResponse.error("Failed to process chat message: " + e.getMessage())));
        }
    }

    private String extractMessage(HttpServletRequest req) {
        // Try reading parameter first (form submit)
        String param = req.getParameter("message");
        if (param != null && !param.trim().isEmpty()) {
            return param;
        }

        // Try reading raw JSON body { "message": "..." }
        try (BufferedReader reader = req.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            if (sb.length() > 0) {
                JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
                if (json.has("message")) {
                    return json.get("message").getAsString();
                }
            }
        } catch (Exception ignored) {
        }

        return null;
    }
}
