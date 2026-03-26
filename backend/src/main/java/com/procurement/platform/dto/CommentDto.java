package com.procurement.platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    @NotBlank(message = "Message is required")
    private String message;

    private Long requestId;
    private Long userId;
    private String userName;
    private Long parentId;
    private Boolean resolved;
    private String createdAt;
}
