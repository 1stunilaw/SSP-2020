package ssp.marketplace.app.api.OrderController;

import org.springframework.http.HttpStatus;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.ResponseOneOrderDtoAdmin;
import ssp.marketplace.app.exceptions.BadRequest;
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


    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneOrderDtoAdmin createOrder(
            @ModelAttribute @Valid RequestOrderDto responseOrderDto,
            HttpServletRequest req
    ) {
        return orderService.createOrder(req, responseOrderDto);
    }

    @RequestMapping(value = "{orderId}", method = RequestMethod.PATCH, consumes = {"multipart/form-data"})
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
        orderService.deleteOrder(id);
    }

    @PostMapping("/{id}/mark-done")//// TODO: 17.11.2020 Добавить победителя и предложение
    @ResponseStatus(HttpStatus.OK)
    public ResponseOneOrderDtoAdmin markDoneOrder(
            @PathVariable UUID id
    ) {
        return orderService.markDoneOrder(id);
    }

//    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
//    public void submit(@ModelAttribute FormDataWithFile formDataWithFile) {
//        System.out.println(formDataWithFile.getName());
//        System.out.println(formDataWithFile.getEmail());
//        System.out.println(formDataWithFile.getFile().getOriginalFilename());
//    }
}
