package ssp.marketplace.app.dto.offer.requestDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@RequiredArgsConstructor
public class RequestOfferDtoUpdate {

    /**
     * Запрос на изменение:
     * Описание
     * Список загруженных документов
     * Документы
     */
    @NotBlank(message = "{description.errors.empty}")
    private String description;

    private List<String> documents;

    private MultipartFile[] files;
}
