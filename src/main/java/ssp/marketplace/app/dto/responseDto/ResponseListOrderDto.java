package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.service.DocumentService;

import java.time.*;
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

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStop;


    public static ResponseListOrderDto responseOrderDtoFromOrder(Order order) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveDocument(order);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }

        ResponseListOrderDto responseListOrderDto = builder()
                .id(order.getId())
                .dateStart(order.getDateStart())
                .dateStop(order.getDateStop())
                .name(order.getName())
                .statusForOrder(order.getStatusForOrder())
                .user(order.getUser().getCustomerDetails().getFio())
                .number(order.getNumber())
                .build();
        List<Tag> tags = order.getTags();
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