package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;

import java.util.*;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByTagName(String tagName);

    List<Tag> findAllByStatusForTagNotIn(final Collection<StateStatus> statusForTags);

    Optional<Tag> findByIdAndStatusForTagNotIn(
            UUID id,
            final Collection<StateStatus> statusForTags
    );
}
