package ssp.marketplace.app.dto;

import lombok.*;
import ssp.marketplace.app.entity.statuses.*;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class CommentDto {
    private UUID id;
    @NotNull(message = "{order.errors.empty}")

    private UUID orderId;
    @NotBlank(message = "{text.errors.empty}")
    @Size(max = 250)
    private String text;
    private StatusForComment status;
    private Timestamp createdAt;
    private CommentAccessLevel accessLevel;
}