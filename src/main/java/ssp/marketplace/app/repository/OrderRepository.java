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

    @Query(value = "select * from (select * from(SELECT * FROM orders left JOIN orders_tags on orders_tags.order_id = orders.id) as o " +
            "left JOIN  tags on o.tag_id = tags.id " +
            "left JOIN customer_details cd on o.user_id = cd.user_id " +
            "WHERE o.status!='DELETED') as final_o " +
            "WHERE CONCAT(fio, ' ', organization_name, ' ', description, ' ', name, ' ', number, ' ', tag_name) ILIKE CONCAT('%',:search,'%');", nativeQuery = true)
    Set<Order> search(@Param("search") String keyword);

    @Query(value = "select * from orders WHERE orders.status!='DELETED' and orders.status=:status", nativeQuery = true)
    Set<Order> filterStatus(@Param("status") String status);

    @Query(value = "select * from (select * from(SELECT * FROM orders left JOIN orders_tags on orders_tags.order_id = orders.id) as o " +
            "left JOIN  tags on o.tag_id = tags.id " +
            "left JOIN customer_details cd on o.user_id = cd.user_id " +
            "WHERE o.status!='DELETED' and o.status=:status) as final_o " +
            "WHERE CONCAT(fio, ' ', organization_name, ' ', description, ' ', name, ' ', number, ' ', tag_name) ILIKE CONCAT('%',:search,'%');", nativeQuery = true)
    Set<Order> searchAndFilterStatus(@Param("search") String keyword, @Param("status") String status);
}
