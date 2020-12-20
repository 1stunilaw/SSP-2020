package ssp.marketplace.app.dto.lawStatus;

import lombok.*;
import ssp.marketplace.app.entity.supplier.LawStatus;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseLawStatusDto implements Serializable {
    private UUID id;
    private String name;

    public ResponseLawStatusDto(LawStatus lawStatus) {
        id = lawStatus.getId();
        name = lawStatus.getName();
    }
}
