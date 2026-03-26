package com.procurement.platform.controller;

import com.procurement.platform.dto.ChatRequest;
import com.procurement.platform.dto.ChatResponse;
import com.procurement.platform.service.AIChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class AIChatController {

    private final AIChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.getChatResponse(request));
    }
}
