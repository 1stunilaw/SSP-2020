package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByStatusForOrderNotIn(Pageable pageable, final Collection<StatusForOrder> statusForOrder);

    Optional<Order> findByIdAndStatusForOrderNotIn(
            UUID id,
            final Collection<StatusForOrder> statusForOrder
    );

    Optional<Order> findByName(String name);

    @Query(value = "select number from orders where name = ?1", nativeQuery = true)
        //// TODO: 23.11.2020 исправить получение номера
    Long getNumber(String name);
}
