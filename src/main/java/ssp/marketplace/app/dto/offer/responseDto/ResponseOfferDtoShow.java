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
public class ResponseOfferDtoShow extends ResponseOfferDtoAbstract {

    private UUID order;

    public static ResponseOfferDtoShow responseOfferDtoFromOffer(Offer offer) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }
        ResponseOfferDtoShow offerDto = new ResponseOfferDtoShow();
        offerDto.setId(offer.getId());
        offerDto.setOrder(offer.getOrder().getId());
        offerDto.setUser(new ResponseSupplierDto(offer.getUser()));
        offerDto.setCreatedAt(offer.getCreatedAt());
        offerDto.setNumber(offer.getNumber());
        offerDto.setDescription(offer.getDescription());
        offerDto.setStateStatus(offer.getStateStatus());
        offerDto.setDocuments(stringDocs);

        return offerDto;
    }
}
