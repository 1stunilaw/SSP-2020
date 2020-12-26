package ssp.marketplace.app.dto.order;

import lombok.*;
import ssp.marketplace.app.dto.tag.ResponseTag;
import ssp.marketplace.app.dto.user.customer.ResponseCustomerDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.sql.Timestamp;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ResponseOneOrderDtoAbstract {

    private UUID id;

    private String name;

    private Long number;

    private List<ResponseTag> tags;

    private StatusForOrder statusForOrder;

    private Timestamp dateStart;

    private String dateStop;

    private String description;

    private List<String> documents;

    private ResponseCustomerDto user;

    public static void setTags(Order order, ResponseOneOrderDtoAbstract orderDto) {
        Set<Tag> tags = order.getTags();
        List<ResponseTag> tagsName = new ArrayList<>();
        if (tags != null) {
            for (Tag tag : tags
            ) {
                tagsName.add(ResponseTag.getResponseTagFromTag(tag));
            }
            orderDto.setTags(tagsName);
        }
    }
}