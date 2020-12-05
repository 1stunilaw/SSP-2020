package ssp.marketplace.app.dto.requestDto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTag {

    @NotBlank(message = "{tagName.errors.empty}")
    @Size(max = 50)
    @Size(min = 1)
    private String tagName;
}
