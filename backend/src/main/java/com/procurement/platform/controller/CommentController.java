package com.procurement.platform.controller;

import com.procurement.platform.dto.CommentDto;
import com.procurement.platform.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable("requestId") Long requestId) {
        return ResponseEntity.ok(commentService.getCommentsByRequest(requestId));
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.addComment(dto));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<CommentDto> resolveComment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.resolveComment(id));
    }
}
