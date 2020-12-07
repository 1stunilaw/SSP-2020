package ssp.marketplace.app.dto.user.customer;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ssp.marketplace.app.dto.user.UserUpdateRequestDto;

import javax.validation.constraints.*;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomerUpdateRequestDto extends UserUpdateRequestDto {
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{fio.errors.regex}")
    @Length(min = 5, max = 150, message = "{fio.errors.length}")
    private String fio;

    @Length(min = 6, max = 20, message = "{phone.errors.length}")
    @Pattern(regexp = "^((8|\\+[0-9]{1,3})[\\-]?)?(\\(?\\d{3}\\)?[\\-]?)?[\\d\\-]{6,15}$", message = "{phone.errors.regex}")
    private String phone;
}
