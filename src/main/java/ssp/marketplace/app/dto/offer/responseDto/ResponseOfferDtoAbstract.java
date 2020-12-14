package ssp.marketplace.app.dto.offer.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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
     * дата изменения
     * документы
     */
    private UUID id;

    private String order;

    private String user;

    private Long number;

    private String description;

    private StatusForOffer statusForOffer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Timestamp updatedAt;

    private List<String> documents;
}
