package ssp.marketplace.app.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;

import java.util.*;

public interface DocumentService {

    List<Document> addNewDocuments(MultipartFile[] multipartFiles, String pathS3, Order order);

    void deleteDocument(String name);

    S3ObjectInputStream downloadFile(String keyName, String path);

    static List<Document> selectOnlyActiveDocument(Order order) {
        List<Document> allDocuments = order.getDocuments();
        List<Document> activeDocuments = new ArrayList<>();
        if (allDocuments != null) {
            for (Document doc : allDocuments
            ) {
                if (doc.getStatusForDocument() == StatusForDocument.ACTIVE) {
                    activeDocuments.add(doc);
                }
            }
        }
        return activeDocuments;
    }
}
