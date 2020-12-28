package ssp.marketplace.app.dto.offer.responseDto;

import lombok.*;
import ssp.marketplace.app.dto.user.supplier.response.ResponseSupplierDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseOfferDto extends ResponseOfferDtoAbstract {

    private Long order;

    public static ResponseOfferDto responseOfferDtoFromOffer(Offer offer) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }
        ResponseOfferDto offerDto = new ResponseOfferDto();
        offerDto.setId(offer.getId());
        offerDto.setOrder(offer.getOrder().getNumber());
        offerDto.setUser(new ResponseSupplierDto(offer.getUser()));
        offerDto.setCreatedAt(offer.getCreatedAt());
        offerDto.setNumber(offer.getNumber());
        offerDto.setDescription(offer.getDescription());
        offerDto.setStateStatus(offer.getStateStatus());
        offerDto.setDocuments(stringDocs);

        return offerDto;
    }
}
