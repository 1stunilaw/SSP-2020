package ssp.marketplace.app.dto.user.supplier.request;

import lombok.*;
import ssp.marketplace.app.entity.user.*;

import javax.validation.constraints.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestSupplierAddAccreditationDto {
    @NotNull(message = "{accreditationStatus.errors.empty}")
    private AccreditationStatus accreditationStatus;
}
