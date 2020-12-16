package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor {

    Page<Order> findByStatusForOrderNotIn(Pageable pageable, final Collection<StatusForOrder> statusForOrder);

    Optional<Order> findByIdAndStatusForOrderNotIn(
            UUID id,
            final Collection<StatusForOrder> statusForOrder
    );

    Optional<Order> findByName(String name);

    @Query(value = "select * from (SELECT * FROM orders_tags " +
            "INNER JOIN orders on orders_tags.order_id = orders.id " +
            "INNER JOIN tags on orders_tags.tag_id = tags.id " +
            "INNER JOIN customer_details cd on orders.user_id = cd.user_id " +
            "WHERE orders.status!='DELETED') as o " +
            "WHERE CONCAT(o.name, ' ', o.organization_name, ' ', o.description, ' ', o.number, ' ', o.date_stop, ' ', o.tag_name, ' ', o.fio) " +
            "LIKE CONCAT('%',:search,'%')", nativeQuery = true)
    Set<Order> search(@Param("search") String keyword);

    @Query(value = "select * from (SELECT * FROM orders_tags " +
            "INNER JOIN orders on orders_tags.order_id = orders.id " +
            "INNER JOIN tags on orders_tags.tag_id = tags.id " +
            "INNER JOIN customer_details cd on orders.user_id = cd.user_id " +
            "WHERE orders.status!='DELETED' and orders.status=:status) as o ", nativeQuery = true)
    Set<Order> filterStatus(@Param("status") String status);

    @Query(value = "select * from (SELECT * FROM orders_tags " +
            "INNER JOIN orders on orders_tags.order_id = orders.id " +
            "INNER JOIN tags on orders_tags.tag_id = tags.id " +
            "INNER JOIN customer_details cd on orders.user_id = cd.user_id " +
            "WHERE orders.status!='DELETED' and orders.status=:status) as o " +
            "WHERE CONCAT(o.name, ' ', o.organization_name, ' ', o.description, ' ', o.number, ' ', o.date_stop, ' ', o.tag_name, ' ', o.fio) " +
            "LIKE CONCAT('%',:search,'%')", nativeQuery = true)
    Set<Order> searchAndFilterStatus(@Param("search") String keyword, @Param("status") String status);
}
