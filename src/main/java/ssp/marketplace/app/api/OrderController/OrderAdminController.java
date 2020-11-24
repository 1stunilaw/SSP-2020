package ssp.marketplace.app.api.OrderController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.repository.OrderRepository;
import ssp.marketplace.app.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @PostMapping("/create")
    public ResponseOrderDto addNewOrder(
            @Valid @NotNull @RequestPart(value = "order") RequestOrderDto responseOrderDto,
            HttpServletRequest req,
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles
    ) {
        if (multipartFiles != null && multipartFiles.length != 0) {
            return orderService.addNewOrderWithDocuments(req, responseOrderDto, multipartFiles);
        } else {
            return orderService.addNewOrder(req, responseOrderDto);
        }
    }

    @PatchMapping("{orderId}")
    public ResponseOrderDto updatePerson(
            @PathVariable("orderId") UUID id,
            @Valid @RequestBody RequestOrderUpdateDto orderUpdateDto
    ) {
        return orderService.editOrder(id, orderUpdateDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteOrder(
            @PathVariable UUID id
    ) {
        orderService.deleteOrder(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/mark-done/{id}")//// TODO: 17.11.2020 Добавить победителя и предложение
    public ResponseOrderDto markDoneOrder(
            @PathVariable UUID id
    ) {
        return orderService.markDoneOrder(id);
    }
}
