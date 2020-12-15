package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;

import java.util.*;

public interface OfferRepository extends JpaRepository<Offer, UUID> {

    Optional<Offer> findByIdAndStatusForOfferNotIn(
            UUID id,
            final Collection<StatusForOffer> statusForOffer
    );

    Page<Offer> findByOrderIdAndAndUserIdAndStatusForOffer(Pageable pageable, UUID OrderId, UUID UserId, StatusForOffer statusForOffer);

    Page<Offer> findByOrderIdAndStatusForOffer(Pageable pageable, UUID OrderId, StatusForOffer statusForOffer);
}