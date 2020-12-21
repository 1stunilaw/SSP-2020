package ssp.marketplace.app.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.entity.statuses.*;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class CommentDto implements Comparable<CommentDto>{
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private String text;
    private StatusForComment status;
    private Timestamp createdAt;
    private CommentAccessLevel accessLevel;
    private String userName;

    @Override
    public int compareTo(CommentDto o) {
        return getCreatedAt().compareTo(o.createdAt);
    }
}