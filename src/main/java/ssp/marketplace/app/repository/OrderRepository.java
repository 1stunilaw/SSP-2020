package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
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
}
