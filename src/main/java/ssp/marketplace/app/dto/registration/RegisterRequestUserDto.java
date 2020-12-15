package ssp.marketplace.app.dto.registration;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.*;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class RegisterRequestUserDto implements Serializable {
    @NotBlank(message = "{email.errors.empty}")
    @Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "{email.errors.validation}"
    )
    @Length(max = 100, message = "{email.errors.length}")
    @Unique(message = "{email.errors.unique}", service = UserService.class, fieldName = "email")
    private String email;


    @NotBlank(message = "{password.errors.empty}")
    @Length(min = 10, max = 25, message = "{password.errors.length}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$)(?=[\\x21-\\x7E]+$).{10,25}$",
            message = "{password.errors.validation}")
    private String password;
}
