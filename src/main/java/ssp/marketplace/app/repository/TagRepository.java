package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssp.marketplace.app.entity.Tag;

import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Tag findByTagName(String tagName);
}
