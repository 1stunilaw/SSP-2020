package ssp.marketplace.app.api.DocumentController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.DocumentService;

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("api/admin/")
public class DocumentAdminController {

    public final DocumentService documentService;

    public final DocumentRepository documentRepository;

    public final OrderRepository orderRepository;

    public DocumentAdminController(
            DocumentService documentService, DocumentRepository documentRepository, OrderRepository orderRepository
    ) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/orders/{id}/upload_file")
    public void addNewDocument(
            @PathVariable UUID id,
            @RequestParam("files") MultipartFile[] multipartFiles
    ) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        String pathS3 = "/" + order.getClass().getSimpleName() + "/" + order.getName();
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3, order);
        order.setDocuments(documents);
        orderRepository.save(order);
    }

    @DeleteMapping("/document/{name}")
    public ResponseEntity deleteDocument(
            @PathVariable String name
    ) {
        documentService.deleteDocument(name);
        return new ResponseEntity(HttpStatus.OK);
    }
}
