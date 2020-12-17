package ssp.marketplace.app.dto.user.customer;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ssp.marketplace.app.dto.user.UserUpdateRequestDto;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.*;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomerUpdateRequestDto extends UserUpdateRequestDto {
    @Pattern(regexp = "^$|^[a-zA-ZА-я][a-zA-ZА-я-.\" ]{4,149}+$", message = "{fio.errors.regex}")
    private String fio;

    @Unique(message = "{phone.errors.unique}", service = UserService.class, fieldName = "customerPhone")
    @Pattern(regexp = "^$|^((8|\\+[0-9]{1,3})[\\-]?)?(\\(?\\d{3}\\)?[\\-]?)?[\\d\\-]{6,15}$", message = "{phone.errors.regex}")
    private String phone;

    @Pattern(regexp = "^(true|false)$", message = "{sendMail.errors.regex}")
    private String sendEmail;
}
