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
    @Size(min = 1)
    private List<@NotBlank (message = "{tagName.errors.empty}") @Size(max = 50)String> tagName;
}
