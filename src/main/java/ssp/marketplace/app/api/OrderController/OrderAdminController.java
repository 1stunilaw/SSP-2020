package ssp.marketplace.app.api.OrderController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("api/admin/orders")
public class OrderAdminController {

    private final OrderService orderService;

    public OrderAdminController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneOrderDtoAdmin createOrder(
            @RequestPart(value = "order", required = false) String responseOrderDto,
            HttpServletRequest req,
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles
    ) {
        return orderService.createOrder(req, responseOrderDto, multipartFiles);
    }

    @RequestMapping(value = "{orderId}", method = RequestMethod.PATCH, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneOrderDtoAdmin updateOrder(
            @PathVariable("orderId") UUID id,
            @RequestPart(value = "order", required = false) String orderUpdateDto,
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles
    ) {
        return orderService.editOrder(id, orderUpdateDto, multipartFiles);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteOrder(
            @PathVariable UUID id
    ) {
        orderService.deleteOrder(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{id}/mark-done")//// TODO: 17.11.2020 Добавить победителя и предложение
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneOrderDtoAdmin markDoneOrder(
            @PathVariable UUID id
    ) {
        return orderService.markDoneOrder(id);
    }
}
