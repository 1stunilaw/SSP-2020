package ssp.marketplace.app.dto.registration.supplier;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ssp.marketplace.app.dto.registration.RegisterRequestUserDto;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.*;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierRegisterRequestDto extends RegisterRequestUserDto {
    @Unique(message = "{companyName.errors.unique}", service = UserService.class, fieldName = "companyName")
    @NotBlank(message = "{companyName.errors.empty}")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9][a-zA-ZА-я0-9-.,&\" ]+$", message = "{companyName.errors.regex}")
    @Length(min = 1, max = 250, message = "{companyName.errors.length}")
    private String companyName;
}
