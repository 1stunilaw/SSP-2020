package ssp.marketplace.app.dto.user.supplier.response;

import lombok.*;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseSupplierForAdminDto extends ResponseSupplierDto {
    private String accreditationStatus;

    public ResponseSupplierForAdminDto(User user) {
        super(user);
        if(user.getSupplierDetails().getAccreditationStatus() != null){
            accreditationStatus = user.getSupplierDetails().getAccreditationStatus().toString();
        }
    }
}
