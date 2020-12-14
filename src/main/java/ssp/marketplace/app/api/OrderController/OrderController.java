package ssp.marketplace.app.api.OrderController;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    private final DocumentService documentService;

    @Autowired
    public OrderController(OrderService orderService, DocumentService documentService) {
        this.orderService = orderService;
        this.documentService = documentService;
    }

    @GetMapping()
    public Page<ResponseListOrderDto> getOrders(
            @PageableDefault(sort = {"dateStart", "statusForOrder"},
                    size = 30, value = 30, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return orderService.getOrders(pageable);
    }

    @GetMapping("/{id}")
    public ResponseOneOrderDtoAbstract getOneOrder(
            HttpServletRequest req,
            @PathVariable UUID id
    ) {
        return orderService.getOneOrder(id, req);
    }

    @GetMapping(value = "/{orderId}/{filename}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getOrderDocument(
            @PathVariable String filename,
            @PathVariable UUID orderId
    ) {
        S3ObjectInputStream s3is = documentService.downloadOrderFile(filename, orderId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE)).cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(new InputStreamResource(s3is));
    }
}
