package com.procurement.platform.controller;

import com.procurement.platform.dto.AIRecommendationDto;
import com.procurement.platform.service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIRecommendationController {

    private final AIRecommendationService recommendationService;

    @GetMapping("/recommend/{requestId}")
    public ResponseEntity<List<AIRecommendationDto>> getRecommendations(@PathVariable("requestId") Long requestId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(requestId));
    }
}
