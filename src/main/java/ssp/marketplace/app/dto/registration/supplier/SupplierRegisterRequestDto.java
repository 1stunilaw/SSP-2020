package ssp.marketplace.app.dto.registration.supplier;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ssp.marketplace.app.dto.registration.RegisterRequestUserDto;

import javax.validation.constraints.*;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierRegisterRequestDto extends RegisterRequestUserDto {
    @NotBlank(message = "{companyName.errors.empty}")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9][a-zA-ZА-я0-9-.,&\" ]+$", message = "{companyName.errors.regex}")
    @Length(min = 1, max = 250, message = "{companyName.errors.length}")
    private String companyName;
}
