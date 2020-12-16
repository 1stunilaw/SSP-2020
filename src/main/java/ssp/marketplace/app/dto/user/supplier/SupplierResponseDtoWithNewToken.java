package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierResponseDtoWithNewToken extends SupplierResponseDto{

    private String token;

    public SupplierResponseDtoWithNewToken(User user, String token) {
        super(user);
        this.token = token;
    }
}
