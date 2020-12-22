package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.statuses.*;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Дто комментария для ответа на запрос
 */
@Getter
@Setter
public class ResponseOneCommentDto implements Comparable<ResponseOneCommentDto>{
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private String text;
    private StateStatus status;
    private Timestamp createdAt;
    private CommentAccessLevel accessLevel;
    private String userName;

    @Override
    public int compareTo(ResponseOneCommentDto o) {
        return getCreatedAt().compareTo(o.createdAt);
    }
}