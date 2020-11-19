package ssp.marketplace.app.api.DocumentController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.DocumentService;

@RestController
@RequestMapping("/admin")
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

    @PostMapping("/orders/{name}/upload_file")
    public void addNewDocument(
            @PathVariable String name,
            @RequestParam("multipart-files") MultipartFile[] multipartFiles
    ) {
        Order order = orderRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        documentService.addNewDocuments(multipartFiles, "/" + name, order);
    }

    @DeleteMapping("/document/{name}")
    public ResponseEntity deleteDocument(
            @PathVariable String name
    ) {
        documentService.deleteDocument(name);
        return new ResponseEntity(HttpStatus.OK);
    }
}
