package ssp.marketplace.app.dto.registration.supplier;

import lombok.*;
import ssp.marketplace.app.dto.user.ResponseUserDto;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseSupplierRegisterDto extends ResponseUserDto {
    private String companyName;

    public ResponseSupplierRegisterDto(User user) {
        super.setId(user.getId());
        super.setEmail(user.getEmail());
        super.setCreatedAt(user.getCreatedAt());
        super.setUpdatedAt(user.getUpdatedAt());

        companyName = user.getSupplierDetails().getCompanyName();
    }
}
