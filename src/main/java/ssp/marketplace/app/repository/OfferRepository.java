package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;

import java.util.*;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    Optional<Offer> findByIdAndStateStatus(UUID id, StateStatus stateStatus);

    Page<Offer> findByOrderIdAndUserIdAndStateStatus(Pageable pageable, UUID OrderId, UUID UserId, StateStatus stateStatus);

    Page<Offer> findByOrderIdAndStateStatus(Pageable pageable, UUID OrderId, StateStatus stateStatus);

    //если номер предложения формируется внутри заказа
    //List<Offer> findByOrderId(UUID OrderId);
}