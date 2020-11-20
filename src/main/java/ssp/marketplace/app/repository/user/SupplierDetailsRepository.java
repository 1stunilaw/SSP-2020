package ssp.marketplace.app.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.supplier.SupplierDetails;

import java.util.UUID;

@Repository
public interface SupplierDetailsRepository extends JpaRepository<SupplierDetails, UUID> {

}
