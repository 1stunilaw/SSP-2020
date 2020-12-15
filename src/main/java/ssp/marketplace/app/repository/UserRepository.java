package ssp.marketplace.app.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Page<User> findByRolesInAndStatus(Pageable pageable, Collection<Role> roles, UserStatus status);

    Boolean existsBySupplierDetails_Inn(String inn);
}
