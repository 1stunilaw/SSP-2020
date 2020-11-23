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
public class ResponseOrderDto {

    private UUID id;

    private String name;

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStop;

    private String user;

    private Long number;

    private StatusForOrder statusForOrder;

    private List<String> documents;

    private String organizationName;

    private String description;

    private List<String> tags;

    public static ResponseOrderDto responseOrderDtoFromOrder(Order order) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveDocument(order);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }

        ResponseOrderDto responseOrderDto = builder()
                .id(order.getId())
                .dateStart(order.getDateStart())
                .dateStop(order.getDateStop())
                .name(order.getName())
                .statusForOrder(order.getStatusForOrder())
                .user(order.getUser().getCustomerDetails().getFio())
                .documents(stringDocs)
                .description(order.getDescription())
                .organizationName(order.getOrganizationName())
                .number(order.getNumber())
                .build();
        List<Tag> tags = order.getTags();
        List<String> tagsName = new ArrayList<>();
        if (tags != null) {
            for (Tag tag : tags
            ) {
                tagsName.add(tag.getTagName());
            }
            responseOrderDto.setTags(tagsName);
        }
        return responseOrderDto;
    }
}