package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierForAdminResponseDto extends SupplierResponseDto {
    private String accreditationStatus;

    public SupplierForAdminResponseDto(User user) {
        super(user);
        if(user.getSupplierDetails().getAccreditationStatus() != null){
            accreditationStatus = user.getSupplierDetails().getAccreditationStatus().toString();
        }
    }
}
