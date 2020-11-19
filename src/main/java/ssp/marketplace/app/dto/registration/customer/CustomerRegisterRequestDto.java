package ssp.marketplace.app.dto.registration.customer;

import lombok.*;
import ssp.marketplace.app.dto.registration.RegisterRequestUserDto;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CustomerRegisterRequestDto extends RegisterRequestUserDto {
    @NotBlank(message = "{fio.errors.empty}")
    private String fio;

    @NotBlank(message = "{phone.errors.empty}")
    private String phone;
}
