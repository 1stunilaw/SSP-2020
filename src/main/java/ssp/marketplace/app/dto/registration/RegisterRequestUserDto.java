package ssp.marketplace.app.dto.registration;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.service.impl.UserServiceImpl;
import ssp.marketplace.app.validation.Unique;

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
    @Unique(message = "{email.errors.unique}", service = UserService.class, fieldName = "email")
    private String email;


    @NotBlank(message = "{password.errors.empty}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_])(?=\\S+$).{10,}$",
            message = "{password.errors.validation}")
    private String password;
}
