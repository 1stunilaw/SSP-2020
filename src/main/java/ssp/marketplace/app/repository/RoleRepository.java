package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.*;

import java.util.*;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
    Page<Role> findByNameIsIn(List<RoleName> names, Pageable pageable);
}
