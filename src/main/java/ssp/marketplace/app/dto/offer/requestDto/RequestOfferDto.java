package ssp.marketplace.app.dto.offer.requestDto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
@RequiredArgsConstructor
public class RequestOfferDto {

    /**
     * Запрос на создание:
     * Описание
     * Документы
     */
    @NotBlank(message = "{description.errors.empty}")
    @NotNull(message = "{description.errors.empty}")
    @Length(min = 1, max = 10000, message = "{description.errors.length}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{description.errors.regex}")
    private String description;

    private MultipartFile[] files;
}
