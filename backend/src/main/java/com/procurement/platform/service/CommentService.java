package com.procurement.platform.service;

import com.procurement.platform.dto.CommentDto;
import com.procurement.platform.entity.Comment;
import com.procurement.platform.entity.PurchaseRequest;
import com.procurement.platform.entity.User;
import com.procurement.platform.exception.ResourceNotFoundException;
import com.procurement.platform.repository.CommentRepository;
import com.procurement.platform.repository.PurchaseRequestRepository;
import com.procurement.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PurchaseRequestRepository requestRepository;
    private final UserRepository userRepository;

    public List<CommentDto> getCommentsByRequest(Long requestId) {
        return commentRepository.findByPurchaseRequestIdOrderByCreatedAtAsc(requestId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(CommentDto dto) {
        PurchaseRequest request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .purchaseRequest(request)
                .user(user)
                .message(dto.getMessage())
                .parentId(dto.getParentId())
                .resolved(false)
                .build();

        return toDto(commentRepository.save(comment));
    }

    public CommentDto resolveComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.setResolved(true);
        return toDto(commentRepository.save(comment));
    }

    private CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setRequestId(comment.getPurchaseRequest().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUserName(comment.getUser().getName());
        dto.setParentId(comment.getParentId());
        dto.setResolved(comment.getResolved());
        dto.setCreatedAt(comment.getCreatedAt() != null ? comment.getCreatedAt().toString() : null);
        return dto;
    }
}
