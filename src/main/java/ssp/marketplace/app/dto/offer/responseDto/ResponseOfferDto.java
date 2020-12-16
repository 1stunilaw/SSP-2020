package ssp.marketplace.app.dto.offer.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Data
public class ResponseOfferDto extends ResponseOfferDtoAbstract {

    /**
     * Информация, которая видна поставщику:
     *
     * Дата создания
     * Документы
     * Описание
     */

    public static ResponseOfferDto responseOfferDtoFromOffer(Offer offer) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }

        ResponseOfferDto offerDto = new ResponseOfferDto();
        offerDto.setId(offer.getId());
        offerDto.setCreatedAt(offer.getCreatedAt());
        offerDto.setDescription(offer.getDescription());
        offerDto.setDocuments(stringDocs);

        return offerDto;
    }
}
