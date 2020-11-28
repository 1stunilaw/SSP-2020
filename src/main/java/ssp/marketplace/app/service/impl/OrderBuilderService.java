package ssp.marketplace.app.service.impl;

import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.repository.TagRepository;

import java.time.*;
import java.util.*;

@Service
public class OrderBuilderService {

    private static final int HOUR = 23;

    private static final int MINUTE = 59;

    private final TagRepository tagRepository;

    public OrderBuilderService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Order orderFromOrderDto(RequestOrderDto requestOrderDto) {
        StatusForOrder statusForOrder = StatusForOrder.WAITING_OFFERS;
        StatusForOrder statusOrderDto = requestOrderDto.getStatusForOrder();
        if (statusOrderDto != null) {
            statusForOrder = statusOrderDto;
        }
        LocalDate localDate = requestOrderDto.getDateStop();
        LocalDateTime localDateTime = localDate.atStartOfDay().withHour(HOUR).withMinute(MINUTE);

        List<String> tags = requestOrderDto.getTags();

        Order order = Order.builder()
                .name(requestOrderDto.getName())
                .dateStart(LocalDateTime.now())
                .dateStop(localDateTime)
                .description(requestOrderDto.getDescription())
                .statusForOrder(statusForOrder)
                .organizationName(requestOrderDto.getOrganizationName())
                .build();
        setTagForOrder(order, tags);
        return order;
    }

    public Order setTagForOrder(Order order, List<String> tags) {
        List<Tag> orderTags = new ArrayList<>();
        if (tags != null) {
            for (String tagName : tags
            ) {
                Tag tagFromDB = tagRepository.findByTagName(tagName);
                tagFromDB.getOrdersList().add(order);
                orderTags.add(tagFromDB);//
                tagRepository.save(tagFromDB);
            }
        }
        order.setTags(orderTags);
        return order;
    }
}

