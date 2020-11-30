package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.*;
import ssp.marketplace.app.entity.Tag;

import java.util.*;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByTagName(String tagName);
    @Query(value = "select tag_name from tags", nativeQuery = true)
    List<String> findAllTagName();
}
