package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

public interface OrderService {


    int MINUTE = 59;
    int HOUR = 23;

    Page<ResponseListOrderDto> getOrders(Pageable pageable);

    ResponseOneOrderDtoAdmin createOrder(HttpServletRequest req, RequestOrderDto requestOrderDto);

    ResponseOneOrderDtoAdmin editOrder(UUID id, RequestOrderUpdateDto updateDto);

    void deleteOrder(UUID id);

    ResponseOneOrderDtoAdmin markDoneOrder(UUID id);

    ResponseOneOrderDtoAbstract getOneOrder(UUID id, HttpServletRequest req);
}
