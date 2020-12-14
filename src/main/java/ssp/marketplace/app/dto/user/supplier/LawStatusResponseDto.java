package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import ssp.marketplace.app.entity.supplier.LawStatus;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class LawStatusResponseDto implements Serializable {
    private UUID id;
    private String name;

    public LawStatusResponseDto(LawStatus lawStatus) {
        id = lawStatus.getId();
        name = lawStatus.getName();
    }
}
