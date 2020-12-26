package ssp.marketplace.app.dto.order;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseListOrderDto {

    private UUID id;

    private String name;

    private Long number;

    private String user;

    private StatusForOrder statusForOrder;

    private List<String> tags;

    private String dateStart;

    private String dateStop;

    public static ResponseListOrderDto responseOrderDtoFromOrder(Order order) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveDocument(order);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }
        ResponseListOrderDto responseListOrderDto = builder()
                .id(order.getId())
                .dateStart(order.getDateStart().withSecond(0).withNano(0).toString())
                .dateStop(order.getDateStop().withSecond(0).withNano(0).toString())
                .name(order.getName())
                .statusForOrder(order.getStatusForOrder())
                .user(order.getUser().getCustomerDetails().getFio())
                .number(order.getNumber())
                .build();
        Set<Tag> tags = order.getTags();
        List<String> tagsName = new ArrayList<>();
        if (tags != null) {
            for (Tag tag : tags
            ) {
                tagsName.add(tag.getTagName());
            }
            responseListOrderDto.setTags(tagsName);
        }
        return responseListOrderDto;
    }
}