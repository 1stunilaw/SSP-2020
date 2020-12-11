package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor{

    Page<Order> findByStatusForOrderNotIn(Pageable pageable, final Collection<StatusForOrder> statusForOrder);

    Optional<Order> findByIdAndStatusForOrderNotIn(
            UUID id,
            final Collection<StatusForOrder> statusForOrder
    );

    Optional<Order> findByName(String name);

    @Query(value = "select number from orders where name = ?1", nativeQuery = true)
        //// TODO: 23.11.2020 исправить получение номера
    Long getNumber(String name);

//    @Query(value = "select * from orders where concat(organization_name,description) like ?1%", nativeQuery = true)
//    List<Order> search(String search);

//    @Query(value = "select * FROM orders AS o "
//            + " WHERE LOWER(o.name) LIKE LOWER(CONCAT('%',:criteria, '%')) OR"
//            + " LOWER(o.organization_name) LIKE LOWER(CONCAT('%',:criteria, '%')) OR"
//            + " LOWER(o.description) LIKE LOWER(CONCAT('%',:criteria, '%')) "
//            + " LOWER(doc.name_document) LIKE LOWER(CONCAT('%',:criteria, '%')) "
//            + " ORDER BY o.name ASC",
//            nativeQuery = true)
//    List<Order> search(@Param("criteria") String search);

}
