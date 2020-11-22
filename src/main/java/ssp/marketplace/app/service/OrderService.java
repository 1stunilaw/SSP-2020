package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    Page<ResponseOrderDto> getOrders(Pageable pageable);

    ResponseOrderDto addNewOrderWithDocuments(HttpServletRequest req, RequestOrderDto requestOrderDto, MultipartFile[] multipartFiles);

    ResponseOrderDto addNewOrder(HttpServletRequest req, RequestOrderDto requestOrderDto);

//    ResponseOrderDto editOrder(String name, RequestOrderDto requestOrderDto);

    void deleteOrder(String name);

    ResponseOrderDto markDoneOrder(String name);

    ResponseOrderDto getOneOrder(String name);

    static Order orderFromOrderDto(RequestOrderDto requestOrderDto) {
        StatusForOrder statusForOrder = StatusForOrder.ACTIVE;
        StatusForOrder statusOrderDto = requestOrderDto.getStatusForOrder();
        if(statusOrderDto != null){
            statusForOrder =  statusOrderDto;
        }
        Order order = Order.builder()
                .dateStart(requestOrderDto.getDateStart())
                .dateStop(requestOrderDto.getDateStop())
                .name(requestOrderDto.getName())
                .statusForOrder(statusForOrder)
                .description(requestOrderDto.getDescription())
                .build();
        return order;
    }
}
