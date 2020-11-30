package ssp.marketplace.app.api.DocumentController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.responseDto.ResponseNameDocument;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.exceptions.NotFoundException;
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
//        System.out.println(multipartFiles[0].getSize());
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        String pathS3 = "/" + order.getClass().getSimpleName() + "/" + order.getName();
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3);
        order.getDocuments().addAll(documents);
        orderRepository.save(order);
        List<String> strings = new ArrayList<>();
        for (Document doc : documents
        ){
            strings.add(doc.getName());
        }
        ResponseNameDocument responseNameDocument = new ResponseNameDocument(strings);
        return responseNameDocument;
    }

    @DeleteMapping("/document/{name}")
    public ResponseEntity deleteDocument(
            @PathVariable String name
    ) {
        documentService.deleteDocument(name);
        return new ResponseEntity(HttpStatus.OK);
    }
}
