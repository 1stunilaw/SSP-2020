package ssp.marketplace.app.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class CommentDto {
    private UUID id;
    private UUID orderId;
    private String text;
    private StatusForComment status;
}
