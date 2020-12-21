package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssp.marketplace.app.entity.Document;
import ssp.marketplace.app.entity.statuses.*;

import java.util.*;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Optional<Document> findByNameAndStatusForDocumentNotIn(String name, Collection<StateStatus> stateStatus);
}
