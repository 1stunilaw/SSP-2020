package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ssp.marketplace.app.entity.user.VerificationToken;

import java.util.*;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, UUID> {

}
