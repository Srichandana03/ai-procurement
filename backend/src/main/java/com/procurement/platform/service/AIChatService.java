package com.procurement.platform.service;

import com.procurement.platform.dto.ChatRequest;
import com.procurement.platform.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIChatService {

    @Value("${app.hf.api-key}")
    private String apiKey;

    @Value("${app.hf.model-id}")
    private String modelId;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SYSTEM_PROMPT = 
        "You are a helpful and professional AI Procurement Assistant for the 'AI Procurement Platform'. " +
        "Your goal is to help users understand the platform, manage purchase requests, suppliers, and orders. " +
        "Keep your answers concise and professional. If you don't know something about the project, suggest contacting support.";

    public ChatResponse getChatResponse(ChatRequest request) {
        String url = "https://router.huggingface.co/v1/chat/completions";

        // Prepare request body in OpenAI format
        Map<String, Object> body = new HashMap<>();
        body.put("model", modelId);
        
        java.util.List<Map<String, String>> messages = new java.util.ArrayList<>();
        
        // System message
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);

        // History
        if (request.getHistory() != null) {
            for (ChatRequest.ChatMessage msg : request.getHistory()) {
                Map<String, String> hMsg = new HashMap<>();
                hMsg.put("role", msg.getRole());
                hMsg.put("content", msg.getContent());
                messages.add(hMsg);
            }
        }

        // Current user message
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", request.getMessage());
        messages.add(userMsg);

        body.put("messages", messages);
        body.put("max_tokens", 512);
        body.put("temperature", 0.7);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> responseBody = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
                java.util.List<Map<String, Object>> choices = (java.util.List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    
                    return ChatResponse.builder()
                            .response(content.trim())
                            .model(modelId)
                            .build();
                }
            }
        } catch (Exception e) {
            return ChatResponse.builder()
                    .response("I'm sorry, I'm having trouble connecting to my AI brain right now. Error: " + e.getMessage())
                    .model(modelId)
                    .build();
        }

        return ChatResponse.builder()
                .response("I received no response from the AI. Please try again.")
                .model(modelId)
                .build();
    }
}
