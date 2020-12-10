package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public interface OrderService {

    int MINUTE = 59;
    int HOUR = 23;

    Page<ResponseListOrderDto> getOrders(Pageable pageable);

    ResponseOneOrderDtoAdmin createOrder(HttpServletRequest req, RequestOrderDto requestOrderDto);

    ResponseOneOrderDtoAdmin editOrder(UUID id, RequestOrderUpdateDto updateDto);

    void deleteOrder(UUID id);

    void deleteOrderTags(UUID id, RequestDeleteTags requestDeleteTags);

    ResponseOneOrderDtoAdmin markDoneOrder(UUID id);

    ResponseOneOrderDtoAbstract getOneOrder(UUID id, HttpServletRequest req);

    void deleteDocumentFromOrder(UUID id, String name);
}
