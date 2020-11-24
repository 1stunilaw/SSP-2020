package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

public interface OrderService {

    int MINUTE = 59;
    int HOUR = 23;

    Page<ResponseOrderDto> getOrders(Pageable pageable);

    ResponseOrderDto addNewOrderWithDocuments(HttpServletRequest req, RequestOrderDto requestOrderDto, MultipartFile[] multipartFiles);

    ResponseOrderDto addNewOrder(HttpServletRequest req, RequestOrderDto requestOrderDto);

    ResponseOrderDto editOrder(UUID id, RequestOrderUpdateDto orderUpdateDto);

    void deleteOrder(UUID id);

    ResponseOrderDto markDoneOrder(UUID id);

    ResponseOrderDto getOneOrder(UUID id);

    static Order orderFromOrderDto(RequestOrderDto requestOrderDto) {
        StatusForOrder statusForOrder = StatusForOrder.WAITING_OFFERS;
        StatusForOrder statusOrderDto = requestOrderDto.getStatusForOrder();
        if(statusOrderDto != null){
            statusForOrder =  statusOrderDto;
        }
        LocalDate localDate = requestOrderDto.getDateStop();
        LocalDateTime localDateTime = localDate.atStartOfDay().withHour(HOUR).withMinute(MINUTE);

        Order order = Order.builder()
                .name(requestOrderDto.getName())
                .dateStart(LocalDateTime.now())
                .dateStop(localDateTime)
                .description(requestOrderDto.getDescription())
                .statusForOrder(statusForOrder)
                .organizationName(requestOrderDto.getOrganizationName())
                .build();
        return order;
    }
}
