package ssp.marketplace.app.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.responseDto.ResponseNameDocument;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.*;

import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final S3Services s3Services;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    public final DocumentRepository documentRepository;

    private final MessageSource messageSource;


    public DocumentServiceImpl(S3Services s3Services, MessageSource messageSource, UserRepository userRepository, OrderRepository orderRepository, DocumentRepository documentRepository) {
        this.s3Services = s3Services;
        this.orderRepository = orderRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    public List<Document> addNewDocuments(MultipartFile[] multipartFiles, String pathS3) {
        List<Document> documents = new ArrayList<>();
        //Проверка выбрал ли пользователь документ
        if (multipartFiles[0].getOriginalFilename().isEmpty()) {
            throw new BadRequestException("Документ не может быть пустым");
        }
        for (MultipartFile mf : multipartFiles
        ) {
            String fileName = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
            s3Services.uploadFile(mf, fileName, pathS3);
            Document document = new Document();
            document.setName(fileName);
            document.setStatusForDocument(StatusForDocument.ACTIVE);
            documents.add(document);
            documentRepository.save(document);
        }
        return documents;
    }

    @Override
    public ResponseNameDocument addNewDocumentsInOrder(
            UUID id, MultipartFile[] multipartFiles
    ) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        int countActiveDocs = DocumentService.selectOnlyActiveDocument(order).size();
        int countNewDocs = multipartFiles.length;
        if (countActiveDocs + countNewDocs > 10) {
            String filesCountError = messageSource.getMessage("files.errors.amount", null, new Locale("ru", "RU"));
            throw new BadRequestException(filesCountError);
        }
        String pathS3 = "/" + order.getClass().getSimpleName() + "/" + order.getName();
        List<Document> documents = addNewDocuments(multipartFiles, pathS3);
        order.getDocuments().addAll(documents);
        orderRepository.save(order);
        List<String> strings = new ArrayList<>();
        for (Document doc : documents
        ) {
            strings.add(doc.getName());
        }
        ResponseNameDocument responseNameDocument = new ResponseNameDocument(strings);
        return responseNameDocument;
    }

    @Override
    public void deleteDocument(String name) {
        Document document = documentRepository.findByNameAndStatusForDocumentNotIn(
                name, Collections.singleton(StatusForDocument.DELETED))
                .orElseThrow(() -> new NotFoundException("Документ не найден"));
        document.setStatusForDocument(StatusForDocument.DELETED);
        documentRepository.save(document);
    }

    @Override
    public S3ObjectInputStream downloadSupplierFile(String keyName, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        String className = user.getClass().getSimpleName().split("\\$")[0];
        String path = "/" + className + "/" + user.getId();
        return s3Services.downloadFile(keyName, path);
    }

    @Override
    public S3ObjectInputStream downloadOrderFile(String keyName, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        String className = order.getClass().getSimpleName().split("\\$")[0];
        String path = "/" + className + "/" + order.getName();
        return s3Services.downloadFile(keyName, path);
    }
}
