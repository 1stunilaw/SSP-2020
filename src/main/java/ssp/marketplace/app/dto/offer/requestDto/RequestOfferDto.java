package ssp.marketplace.app.dto.offer.requestDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class RequestOfferDto {

    /**
     * Запрос на создание:
     * Описание
     * Документы
     */
    @NotBlank(message = "{description.errors.empty}")
    private String description;

    private MultipartFile[] files;
}
