package ssp.marketplace.app.api.DocumentController;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;
import ssp.marketplace.app.exceptions.NotFoundException;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.Collections;

@RestController
@RequestMapping("api/document")
public class DocumentController {

    public final DocumentService documentService;

    public final DocumentRepository documentRepository;

    public final OrderRepository orderRepository;

    public DocumentController(
            DocumentService documentService, DocumentRepository documentRepository, OrderRepository orderRepository
    ) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping(value = "/{filename}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> getDocument(
            @PathVariable String filename
    ) {
        Document document = documentRepository.findByNameAndStatusForDocumentNotIn(
                filename, Collections.singleton(StatusForDocument.DELETED))
                .orElseThrow(() -> new NotFoundException("Документ не найден"));
        Order order = document.getOrdersList().get(0);
        String className = order.getClass().getSimpleName().split("\\$")[0];
        String path = "/" + className + "/" + order.getName();

        S3ObjectInputStream s3is = documentService.downloadFile(filename, path);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE)).cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(new InputStreamResource(s3is));
    }
}
