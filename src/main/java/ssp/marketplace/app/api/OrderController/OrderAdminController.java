package ssp.marketplace.app.api.OrderController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.service.OrderService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/orders")
public class OrderAdminController {

    private final OrderService orderService;

    public OrderAdminController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseOrderDto addNewOrder(
            @RequestPart(value = "order") RequestOrderDto responseOrderDto,
            HttpServletRequest req,
            @RequestPart(value = "multipart-files", required = false) MultipartFile[] multipartFiles
    ) {
        if (multipartFiles != null && multipartFiles.length != 0) {
            return orderService.addNewOrderWithDocuments(req, responseOrderDto, multipartFiles);
        } else {
            return orderService.addNewOrder(req, responseOrderDto);
        }
    }

//    @PutMapping("/{name}")
//    public ResponseOrderDto editOrder(
//            @PathVariable String name,
//            @RequestBody RequestOrderDto responseOrderDto
//    ) {
//        return orderService.editOrder(name, responseOrderDto);
//    }

    @DeleteMapping(value = "/q/{name}")
    public ResponseEntity deleteOrder(
            @PathVariable String name
    ) {
        orderService.deleteOrder(name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/mark-done/{name}")//// TODO: 17.11.2020 Добавить победителя и предложение
    public ResponseOrderDto markDoneOrder(
            @PathVariable String name
    ) {
        return orderService.markDoneOrder(name);
    }
}
