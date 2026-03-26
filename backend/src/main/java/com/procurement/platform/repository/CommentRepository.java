package com.procurement.platform.repository;

import com.procurement.platform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPurchaseRequestIdOrderByCreatedAtAsc(Long requestId);
    List<Comment> findByPurchaseRequestIdAndParentIdIsNullOrderByCreatedAtAsc(Long requestId);
    List<Comment> findByParentId(Long parentId);
}
