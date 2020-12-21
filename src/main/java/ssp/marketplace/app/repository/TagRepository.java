package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssp.marketplace.app.entity.Tag;
import ssp.marketplace.app.entity.statuses.StateStatus;

import java.util.*;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Optional<Tag> findByTagName(String tagName);

    List<Tag> findAllByStatusForTagNotIn(final Collection<StateStatus> statusForTags);

    Optional<Tag> findByIdAndStatusForTagNotIn(
            UUID id,
            final Collection<StateStatus> statusForTags
    );
}
