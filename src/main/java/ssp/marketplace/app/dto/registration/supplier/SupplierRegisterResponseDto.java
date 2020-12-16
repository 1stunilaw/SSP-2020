package ssp.marketplace.app.dto.registration.supplier;

import lombok.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierRegisterResponseDto extends UserResponseDto {
    private String companyName;

    public SupplierRegisterResponseDto(User user) {
        super.setId(user.getId());
        super.setEmail(user.getEmail());
        super.setCreatedAt(user.getCreatedAt());
        super.setUpdatedAt(user.getUpdatedAt());

        companyName = user.getSupplierDetails().getCompanyName();
    }
}
