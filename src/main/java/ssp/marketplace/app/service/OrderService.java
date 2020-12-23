package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.order.*;
import ssp.marketplace.app.dto.tag.RequestDeleteTags;
import ssp.marketplace.app.validation.unique.FieldValueExists;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public interface OrderService{

    Page<ResponseListOrderDto> getOrders(Pageable pageable, String textSearch, String status);

    ResponseOneOrderDtoAdmin createOrder(HttpServletRequest req, RequestOrderDto requestOrderDto);

    ResponseOneOrderDtoAdmin editOrder(UUID id, RequestOrderUpdateDto updateDto);

    void deleteOrder(UUID id);

    void deleteOrderTags(UUID id, RequestDeleteTags requestDeleteTags);

    ResponseOneOrderDtoAdmin markDoneOrder(UUID id);

    ResponseOneOrderDtoAbstract getOneOrder(UUID id, HttpServletRequest req);

    void deleteDocumentFromOrder(UUID id, String name);
}
