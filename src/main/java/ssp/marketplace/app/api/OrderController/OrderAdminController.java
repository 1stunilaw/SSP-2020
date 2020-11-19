package ssp.marketplace.app.api.OrderController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.RequestOrderDto;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;
import ssp.marketplace.app.exceptions.NotFoundException;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/admin/orders")
public class OrderAdminController {

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final DocumentRepository documentRepository;

    public OrderAdminController(
            OrderService orderService, JwtTokenProvider jwtTokenProvider,
            DocumentRepository documentRepository
    ) {
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.documentRepository = documentRepository;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseOrderDto addNewOrder(
            @RequestPart(value = "order") RequestOrderDto responseOrderDto, HttpServletRequest req,
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
