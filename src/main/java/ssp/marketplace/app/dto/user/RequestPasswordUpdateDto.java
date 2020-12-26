package ssp.marketplace.app.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestPasswordUpdateDto implements Serializable {
    @NotBlank(message = "{password.errors.empty}")
    @Length(min = 10, max = 25, message = "{password.errors.length}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$)(?=[\\x21-\\x7E]+$).{10,25}$",
            message = "{password.errors.validation}")
    private String password;

    @NotBlank(message = "{passwordConfirm.errors.empty}")
    private String passwordConfirm;
}
