package com.rahimunisha.rahimunishamart.dto;

import java.io.Serializable;

public class ChatResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reply;
    private boolean cached;
    private String provider;

    public ChatResponseDTO() {
    }

    public ChatResponseDTO(String reply, boolean cached, String provider) {
        this.reply = reply;
        this.cached = cached;
        this.provider = provider;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
