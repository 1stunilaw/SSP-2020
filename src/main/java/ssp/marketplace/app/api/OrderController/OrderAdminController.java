package ssp.marketplace.app.api.OrderController;

import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("api/admin/orders")
public class OrderAdminController {

    private final OrderService orderService;

    public OrderAdminController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    // TODO: 20.12.2020 Переделать аннотации
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneOrderDtoAdmin createOrder(
            @ModelAttribute @Valid RequestOrderDto responseOrderDto,
            HttpServletRequest req
    ) {
        return orderService.createOrder(req, responseOrderDto);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.PATCH, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseOneOrderDtoAdmin updateOrder(
            @PathVariable("orderId") UUID id,
            @ModelAttribute @Valid RequestOrderUpdateDto updateDto
    ) {
        return orderService.editOrder(id, updateDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(
            @PathVariable UUID id
    ) {
        // TODO: 20.12.2020 Добавить ответ
        orderService.deleteOrder(id);
    }

    @PostMapping("/{id}/mark-done")
    @ResponseStatus(HttpStatus.OK)
    public ResponseOneOrderDtoAdmin markDoneOrder(
            @PathVariable UUID id
    ) {
        return orderService.markDoneOrder(id);
    }

    @DeleteMapping(value = "/{orderId}/delete-tag")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTagFromOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid RequestDeleteTags requestDeleteTags
    ) {
        orderService.deleteOrderTags(orderId, requestDeleteTags);
    }

    @DeleteMapping("/{orderId}/document/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDocumentFromOrder(
            @PathVariable UUID orderId,
            @PathVariable String name
    ) {
        // TODO: 20.12.2020 Добавить ответ
        orderService.deleteDocumentFromOrder(orderId, name);
    }
}
