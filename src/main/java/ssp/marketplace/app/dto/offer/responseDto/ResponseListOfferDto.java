package ssp.marketplace.app.dto.offer.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.sql.Timestamp;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseListOfferDto{

    private UUID id;

    private Long number;

    private UUID user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Timestamp createdAt;

    public static ResponseListOfferDto responseOfferDtoFromOffer(Offer offer) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }
        ResponseListOfferDto responseListOfferDto = builder()
                .id(offer.getId())
                .number(offer.getNumber())
                .user(offer.getUser().getId())
                .createdAt(offer.getCreatedAt())
                .build();

        return responseListOfferDto;
    }
}
