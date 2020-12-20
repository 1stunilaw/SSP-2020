package ssp.marketplace.app.dto.requestDto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTag {

    // TODO: 20.12.2020 ???
    @NotBlank(message = "{tagName.errors.empty}")
    @Size(min = 1, max = 50, message = "{tagName.errors.length}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{tagName.errors.regex}")
    private String tagName;
}
