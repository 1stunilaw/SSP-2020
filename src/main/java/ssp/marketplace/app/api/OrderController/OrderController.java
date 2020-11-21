package ssp.marketplace.app.api.OrderController;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public Page<ResponseOrderDto> getOrders(
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return orderService.getOrders(pageable);
    }

    @GetMapping("/{name}")
    public ResponseOrderDto getOneOrder(
            @PathVariable String name
    ) {
        return orderService.getOneOrder(name);
    }
}
