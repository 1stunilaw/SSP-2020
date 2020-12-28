package ssp.marketplace.app.dto.offer.responseDto;

import lombok.*;
import ssp.marketplace.app.dto.user.supplier.response.ResponseSupplierDto;
import ssp.marketplace.app.entity.statuses.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Data
@RequiredArgsConstructor
public abstract class ResponseOfferDtoAbstract implements Serializable {

    private UUID id;

    private ResponseSupplierDto user;

    private Long number;

    private String description;

    private StateStatus stateStatus;

    private Timestamp createdAt;

    //private Timestamp updatedAt;

    private List<String> documents;
}
