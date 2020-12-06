package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;

import java.util.List;
import java.util.stream.Collectors;

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
