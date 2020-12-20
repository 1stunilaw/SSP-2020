package ssp.marketplace.app.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationRequestDto implements Serializable {

    // TODO: 20.12.2020 Добавить сообщения
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
