package ssp.marketplace.app.dto.requestDto;

import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTag {

    @NotNull(message = "{tagName.errors.empty}")
    @Size(min = 1, message = "{tagName.errors.empty}")
    private List<
            @NotBlank(message = "{tagName.errors.empty}")
            @Size(min = 1, max = 50, message = "{tagName.errors.length}")
            @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-.,!?'&\" ]+$)", message = "{tagName.errors.regex}")
                    String> tagName;
}
