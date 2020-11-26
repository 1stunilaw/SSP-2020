package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.UUID;

public interface OrderService {

    int MINUTE = 59;
    int HOUR = 23;

    Page<ResponseListOrderDto> getOrders(Pageable pageable);

    ResponseOneOrderDtoAdmin createOrder(HttpServletRequest req, String requestOrderDto, MultipartFile[] multipartFiles);

    ResponseOneOrderDtoAdmin editOrder(UUID id, String dtoString, MultipartFile[] multipartFiles);

    void deleteOrder(UUID id);

    ResponseListOrderDto markDoneOrder(UUID id);

    ResponseOneOrderDtoAbstract getOneOrder(UUID id, HttpServletRequest req);

    static Order orderFromOrderDto(RequestOrderDto requestOrderDto) {
        StatusForOrder statusForOrder = StatusForOrder.WAITING_OFFERS;
        StatusForOrder statusOrderDto = requestOrderDto.getStatusForOrder();
        if (statusOrderDto != null) {
            statusForOrder = statusOrderDto;
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
