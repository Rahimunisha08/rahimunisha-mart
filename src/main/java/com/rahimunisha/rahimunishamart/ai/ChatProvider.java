package com.rahimunisha.rahimunishamart.ai;

/**
 * ChatProvider Interface (Section 17)
 * Decouples AI implementation following Strategy & Factory design patterns.
 */
public interface ChatProvider {
    String generateReply(String userMessage) throws Exception;
    String getProviderName();
}
