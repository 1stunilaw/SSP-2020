package ssp.marketplace.app.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.DocumentRepository;
import ssp.marketplace.app.service.*;

import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final S3Services s3Services;

    public final DocumentRepository documentRepository;

    public DocumentServiceImpl(S3Services s3Services, DocumentRepository documentRepository) {
        this.s3Services = s3Services;
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> addNewDocuments(MultipartFile[] multipartFiles, String pathS3, Order order) {
        List<Document> documents = new ArrayList<>();
        //Проверка выбрал ли пользователь документ
        if (multipartFiles[0].getOriginalFilename().isEmpty()) {
            System.out.println("test");
            throw new BadRequest("Документ не может быть пустым");
        }
        for (MultipartFile mf : multipartFiles
        ) {
            String fileName = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
            s3Services.uploadFile(mf, fileName, pathS3);
            Document document = new Document();
            document.setName(fileName);
            document.setStatusForDocument(StatusForDocument.ACTIVE);
            document.setOrdersList(Collections.singletonList(order));
            documents.add(document);
            documentRepository.save(document);
        }
        return documents;
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
    public S3ObjectInputStream downloadFile(String keyName, String path) {
        return s3Services.downloadFile(keyName, path);
    }
}
