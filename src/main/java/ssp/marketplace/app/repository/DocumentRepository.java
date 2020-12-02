package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssp.marketplace.app.entity.Document;
import ssp.marketplace.app.entity.statuses.*;

import java.util.*;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Document findByName(String name);
    Optional<Document> findByNameAndStatusForDocumentNotIn(String name,
            Collection<StatusForDocument> statusForDocuments);
}
