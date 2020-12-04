package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.supplier.LawStatus;

import java.util.UUID;

@Repository
public interface LawStatusRepository extends JpaRepository<LawStatus, UUID> {

}
