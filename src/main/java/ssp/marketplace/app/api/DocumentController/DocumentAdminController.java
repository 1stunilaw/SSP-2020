package ssp.marketplace.app.api.DocumentController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.responseDto.ResponseNameDocument;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.DocumentService;

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
    @ResponseStatus(HttpStatus.OK)
    public ResponseNameDocument addNewDocument(
            @PathVariable UUID id,
            @RequestParam("files") MultipartFile[] multipartFiles
    ) {
        return documentService.addNewDocumentsInOrder(id, multipartFiles);
    }
}
