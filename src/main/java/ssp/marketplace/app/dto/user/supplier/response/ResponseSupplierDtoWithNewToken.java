package ssp.marketplace.app.dto.user.supplier.response;

import lombok.*;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseSupplierDtoWithNewToken extends ResponseSupplierDto {

    private String token;

    public ResponseSupplierDtoWithNewToken(User user, String token) {
        super(user);
        this.token = token;
    }
}
