package com.procurement.platform.controller;

import com.procurement.platform.config.JwtUtil;
import com.procurement.platform.dto.PurchaseRequestDto;
import com.procurement.platform.service.PurchaseRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class PurchaseRequestController {

    private final PurchaseRequestService requestService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<PurchaseRequestDto>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("/my")
    public ResponseEntity<List<PurchaseRequestDto>> getMyRequests(HttpServletRequest request) {
        Long userId = extractUserId(request);
        return ResponseEntity.ok(requestService.getRequestsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequestDto> getRequestById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @PostMapping
    public ResponseEntity<PurchaseRequestDto> createRequest(
            @Valid @RequestBody PurchaseRequestDto dto,
            HttpServletRequest request) {
        Long userId = extractUserId(request);
        return ResponseEntity.ok(requestService.createRequest(dto, userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PurchaseRequestDto> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(requestService.updateRequestStatus(id, body.get("status")));
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
