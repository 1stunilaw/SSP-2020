package ssp.marketplace.app.dto.user.customer;

import lombok.*;
import ssp.marketplace.app.dto.user.RequestUserUpdateDto;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.*;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestCustomerUpdateDto extends RequestUserUpdateDto {
    @Pattern(regexp = "^$|^[a-zA-ZА-я][a-zA-ZА-я-.\" ]{4,149}+$", message = "{fio.errors.regex}")
    private String fio;

    @Unique(message = "{phone.errors.unique}", service = UserService.class, fieldName = "customerPhone")
    @Pattern(regexp = "^$|^((8|\\+[0-9]{1,3})[\\-]?)?(\\(?\\d{3}\\)?[\\-]?)?[\\d\\-]{6,15}$", message = "{phone.errors.regex}")
    private String phone;
}
