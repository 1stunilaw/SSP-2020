package ssp.marketplace.app.dto.requestDto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTag {

    @NotBlank(message = "{tagName.errors.empty}")
    @Size(min = 1, max = 50, message = "{tagName.errors.length}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-.,&\" ]+$)", message = "{tagName.errors.regex}")
    private String tagName;
}
