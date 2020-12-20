package ssp.marketplace.app.dto.auth;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestAuthenticationDto implements Serializable {

    // TODO: 20.12.2020 Добавить сообщения
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
