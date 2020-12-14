package ssp.marketplace.app.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.responseDto.ResponseNameDocument;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;

import java.util.*;

public interface DocumentService {

    List<Document> addNewDocuments(MultipartFile[] multipartFiles, String pathS3);

    ResponseNameDocument addNewDocumentsInOrder(UUID id, MultipartFile[] multipartFiles);

    void deleteDocument(String name);

    S3ObjectInputStream downloadSupplierFile(String keyName, UUID userId);

    S3ObjectInputStream downloadOrderFile(String keyName, UUID orderId);

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

    static List<Document> selectOnlyActiveOfferDocument(Offer offer) { //TODO: подкорректировать позже
        List<Document> allDocuments = offer.getDocuments();
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
