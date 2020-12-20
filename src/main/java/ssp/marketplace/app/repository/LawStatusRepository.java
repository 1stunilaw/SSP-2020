package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.statuses.StateStatus;
import ssp.marketplace.app.entity.supplier.LawStatus;

import java.util.*;

@Repository
public interface LawStatusRepository extends JpaRepository<LawStatus, UUID> {
    Set<LawStatus> findByStatus(StateStatus status);
    Optional<LawStatus> findByIdAndStatus(UUID id, StateStatus status);
    Boolean existsByName(String name);
    Set<LawStatus> findByNameAndStatus(String name, StateStatus status);
}
