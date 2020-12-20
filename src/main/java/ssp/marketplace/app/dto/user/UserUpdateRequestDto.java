package ssp.marketplace.app.dto.user;

import lombok.*;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class UserUpdateRequestDto implements Serializable {
    @Pattern(regexp = "^(true|false)$", message = "{sendMail.errors.regex}")
    private String sendEmail;
}
