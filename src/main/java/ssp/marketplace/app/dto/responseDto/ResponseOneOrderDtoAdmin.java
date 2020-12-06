package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseOneOrderDtoAdmin extends ResponseOneOrderDtoAbstract {

    private String organizationName;

    public static ResponseOneOrderDtoAdmin responseOrderDtoFromOrder(Order order) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveDocument(order);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }

        ResponseOneOrderDtoAdmin orderDto = new ResponseOneOrderDtoAdmin();
        orderDto.setId(order.getId());
        orderDto.setDateStart(order.getDateStart().withSecond(0).withNano(0).toString());
        orderDto.setDateStop(order.getDateStop().withSecond(0).withNano(0).toString());
        orderDto.setName(order.getName());
        orderDto.setUser(order.getUser().getCustomerDetails().getFio());
        orderDto.setStatusForOrder(order.getStatusForOrder());
        orderDto.setDocuments(stringDocs);
        orderDto.setDescription(order.getDescription());
        orderDto.setNumber(order.getNumber());
        orderDto.setOrganizationName(order.getOrganizationName());

        Set<Tag> tags = order.getTags();
        List<String> tagsName = new ArrayList<>();
        if (tags != null) {
            for (Tag tag : tags
            ) {
                tagsName.add(tag.getTagName());
            }
            orderDto.setTags(tagsName);
        }
        return orderDto;
    }
}