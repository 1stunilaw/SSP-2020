package ssp.marketplace.app.dto.requestDto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ssp.marketplace.app.entity.statuses.CommentAccessLevel;

import javax.validation.constraints.*;
import java.util.UUID;

@Getter
@Setter
public class RequestCommentDto {
    @NotNull(message = "{order.errors.empty}")
    private UUID orderId;

    @NotBlank(message = "{text.errors.empty}")
    @Length(max = 500, message = "{text.errors.length}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{text.errors.regex}")
    private String text;
    private CommentAccessLevel accessLevel;
}
