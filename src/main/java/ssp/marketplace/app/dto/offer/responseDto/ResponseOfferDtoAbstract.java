package ssp.marketplace.app.dto.offer.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ssp.marketplace.app.dto.user.supplier.SupplierResponseDto;
import ssp.marketplace.app.entity.statuses.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Data
@RequiredArgsConstructor
public abstract class ResponseOfferDtoAbstract implements Serializable {

    /**
     * id предложения
     * id заказа
     * id поставщика (организация)
     * порядковый номер предложения
     * описание
     * статус
     * дата создания
     * //дата изменения
     * документы
     */
    private UUID id;

    private SupplierResponseDto user;

    private Long number;

    private String description;

    private StatusForOffer statusForOffer;

    private Timestamp createdAt;

    // TODO: 20.12.2020 Пофиксить ошибки со времен
    //private Timestamp updatedAt;

    private List<String> documents;
}
