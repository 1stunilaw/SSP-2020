package ssp.marketplace.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.NotFoundException;
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

        List<UUID> tagsId = requestOrderDto.getTags();

        String organizationName = null;
        if(!StringUtils.isBlank(requestOrderDto.getOrganizationName())){
            organizationName = requestOrderDto.getOrganizationName();
        }

        Order order = Order.builder()
                .name(requestOrderDto.getName())
                .dateStart(LocalDateTime.now())
                .dateStop(localDateTime)
                .description(requestOrderDto.getDescription())
                .statusForOrder(statusForOrder)
                .organizationName(organizationName)
                .build();
        setTagForOrder(order, tagsId);
        return order;
    }

    public Order setTagForOrder(Order order, List<UUID> tagsId) {
        System.out.println(tagsId.size());
        Set<Tag> orderTags = order.getTags();
        if(orderTags==null){
            orderTags = new HashSet<>();
        }
        if (tagsId != null) {
            for (UUID id : tagsId
            ) {
                if(id==null){
                    continue;
                }
                Tag tagFromDB = tagRepository.findById(id).orElseThrow(
                        () -> new NotFoundException("Тега c id = " + id + " не существует в базе данных"));
                    orderTags.add(tagFromDB);//
                    tagRepository.save(tagFromDB);
            }
        }
        order.setTags(orderTags);
        return order;
    }
}

