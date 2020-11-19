package ssp.marketplace.app.dto.registration.supplier;

import lombok.*;
import ssp.marketplace.app.dto.registration.RegisterRequestUserDto;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierRegisterRequestDto extends RegisterRequestUserDto {
    @NotBlank(message = "{companyName.errors.empty}")
    private String companyName;
}
