package ssp.marketplace.app.dto.offer.responseDto;

import lombok.*;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseOfferDto extends ResponseOfferDtoAbstract {

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

    private Long order;

    // nit:TODO: 20.12.2020 Переделать через конструктор
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
        offerDto.setUser(new SupplierResponseDto(offer.getUser()));
        offerDto.setCreatedAt(offer.getCreatedAt());
        //offerDto.setUpdatedAt(offer.getUpdatedAt());
        offerDto.setNumber(offer.getNumber());
        offerDto.setDescription(offer.getDescription());
        offerDto.setStatusForOffer(offer.getStatusForOffer());
        offerDto.setDocuments(stringDocs);

        return offerDto;
    }
}
