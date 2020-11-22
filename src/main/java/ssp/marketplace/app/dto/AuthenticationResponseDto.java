package ssp.marketplace.app.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationResponseDto implements Serializable {
    private String email;
    private String token;

    public AuthenticationResponseDto(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
