package ssp.marketplace.app.dto.order;

import ssp.marketplace.app.dto.user.customer.ResponseCustomerDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

public class ResponseOneOrderDto extends ResponseOneOrderDtoAbstract {

    public static ResponseOneOrderDto responseOrderDtoFromOrder(Order order) {
        List<Document> activeDocument = DocumentService.selectOnlyActiveDocument(order);
        List<String> stringDocs = new ArrayList<>();
        for (Document doc : activeDocument
        ) {
            stringDocs.add(doc.getName());
        }
        ResponseOneOrderDto orderDto = new ResponseOneOrderDto();
        orderDto.setId(order.getId());
        orderDto.setDateStart(order.getDateStart().withSecond(0).withNano(0).toString());
        orderDto.setDateStop(order.getDateStop().withSecond(0).withNano(0).toString());
        orderDto.setName(order.getName());
        orderDto.setStatusForOrder(order.getStatusForOrder());
        orderDto.setDocuments(stringDocs);
        orderDto.setDescription(order.getDescription());
        orderDto.setNumber(order.getNumber());
        orderDto.setUser(new ResponseCustomerDto(order.getUser()));
        setTags(order, orderDto);
        return orderDto;
    }
}