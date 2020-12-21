package ssp.marketplace.app.api.OrderController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.dto.order.*;
import ssp.marketplace.app.dto.tag.RequestDeleteTags;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/admin/orders")
public class OrderAdminController {

    private final OrderService orderService;

    public final DocumentService documentService;

    public OrderAdminController(
            OrderService orderService,
            DocumentService documentService
    ) {
        this.orderService = orderService;
        this.documentService = documentService;
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneOrderDtoAdmin createOrder(
            @ModelAttribute @Valid RequestOrderDto responseOrderDto,
            HttpServletRequest req
    ) {
        return orderService.createOrder(req, responseOrderDto);
    }

    @PatchMapping(value = "/{orderId}", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseOneOrderDtoAdmin updateOrder(
            @PathVariable("orderId") UUID id,
            @ModelAttribute @Valid RequestOrderUpdateDto updateDto
    ) {
        return orderService.editOrder(id, updateDto);
    }

    @DeleteMapping(value = "/{id}")
    public SimpleResponse deleteOrder(
            @PathVariable UUID id
    ) {
        orderService.deleteOrder(id);
        return new SimpleResponse(HttpStatus.OK.value(), "Заказ успешно удалён");
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
    public SimpleResponse deleteTagFromOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid RequestDeleteTags requestDeleteTags
    ) {
        orderService.deleteOrderTags(orderId, requestDeleteTags);
        return new SimpleResponse(HttpStatus.OK.value(), "Тег успешно удалён");
    }

    @DeleteMapping("/{orderId}/document/{name}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteDocumentFromOrder(
            @PathVariable UUID orderId,
            @PathVariable String name
    ) {
        orderService.deleteDocumentFromOrder(orderId, name);
        return new SimpleResponse(HttpStatus.OK.value(), "Документ успешно удалён");
    }

    @PostMapping(value = "/{id}/upload_file" , consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseAddNewDocumentInOrder addNewDocument(
            @PathVariable UUID id,
            @RequestParam("files") MultipartFile[] multipartFiles
    ) {
        return documentService.addNewDocumentsInOrder(id, multipartFiles);
    }
}
