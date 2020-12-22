package ssp.marketplace.app.dto.user.supplier.response;

import lombok.*;
import ssp.marketplace.app.entity.user.AccreditationStatus;

@Getter
@Setter
public class ResponseAllAccreditationStatus {

    private AccreditationStatus[] accreditationStatuses;

    public ResponseAllAccreditationStatus() {
        this.accreditationStatuses = AccreditationStatus.values();
    }
}
