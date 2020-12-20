package ssp.marketplace.app.dto.lawStatus;

import lombok.*;
import ssp.marketplace.app.service.LawStatusService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestLawStatusCreateDto implements Serializable {
    @Unique(service = LawStatusService.class, fieldName = "name", message = "Юридический статус с данным названием уже существует")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я\"][a-zA-ZА-я0-9-.,&\" ]{1,99}+$", message = "{lawStatus.errors.regex}")
    private String name;
}
