package ssp.marketplace.app.repository;


import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.Comment;

import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c WHERE c.order.id = ?1 AND c.question IS NULL")
    List<Comment> findByOrderId(UUID orderId);

    @Query("SELECT c FROM Comment c WHERE c.order.id = ?1 AND c.user.id = ?2 AND c.accessLevel = 'PRIVATE' AND c.question IS NULL")
    List<Comment> findAllByOrderIdAndUserId(UUID orderId, UUID userId);

    @Query("SELECT c FROM Comment c WHERE c.order.id = ?1 AND  c.accessLevel = 'PUBLIC' AND c.question IS NULL")
    List<Comment> findAllByOrderIdPublic(UUID orderId);
}
