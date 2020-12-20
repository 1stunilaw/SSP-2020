package ssp.marketplace.app.dto.registration.customer;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ssp.marketplace.app.dto.registration.RequestRegisterUserDto;

import javax.validation.constraints.*;

@Getter
@Setter
public class RequestCustomerRegisterDto extends RequestRegisterUserDto {
    @NotBlank(message = "{fio.errors.empty}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{fio.errors.regex}")
    @Length(min = 5, max = 150, message = "{fio.errors.length}")
    private String fio;

    @NotBlank(message = "{phone.errors.empty}")
    @Length(min = 6, max = 20, message = "{phone.errors.length}")
    @Pattern(regexp = "^((8|\\+[0-9]{1,3})[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{6,15}$", message = "{phone.errors.regex}")
    private String phone;
}
