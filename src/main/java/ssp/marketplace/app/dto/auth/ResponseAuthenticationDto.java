package ssp.marketplace.app.dto.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseAuthenticationDto implements Serializable {
    private String email;
    private String token;

    public ResponseAuthenticationDto(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
