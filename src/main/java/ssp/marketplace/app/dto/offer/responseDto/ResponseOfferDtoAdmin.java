package ssp.marketplace.app.dto.offer.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Data
@Builder
public class ResponseOfferDtoAdmin extends ResponseOfferDtoAbstract {

    /**
     * Информация, которая видна заказчику:
     *
     * id предложения
     * id заказа (номер заказа)
     * id поставщика (организация)
     * порядковый номер предложения
     * описание
     * статус
     * дата создания
     * дата изменения
     * документы
     */
    public static ResponseOfferDtoAdmin responseOfferDtoFromOffer(Offer offer) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }
        ResponseOfferDtoAdmin offerDto = new ResponseOfferDtoAdmin();
        offerDto.setId(offer.getId());
        offerDto.setOrder(offer.getOrder().getNumber().toString());
        offerDto.setUser(offer.getUser().getSupplierDetails().getCompanyName());
        offerDto.setCreatedAt(offer.getCreatedAt());
        offerDto.setUpdatedAt(offer.getUpdatedAt());
        offerDto.setNumber(offer.getNumber());
        offerDto.setDescription(offer.getDescription());
        offerDto.setStatusForOffer(offer.getStatusForOffer());
        offerDto.setDocuments(stringDocs);

        return offerDto;
    }
}
