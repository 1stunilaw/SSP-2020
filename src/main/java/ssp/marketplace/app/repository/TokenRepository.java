package ssp.marketplace.app.repository;

import org.springframework.data.jpa.repository.*;
import ssp.marketplace.app.entity.VerificationToken;

import java.util.*;

public interface TokenRepository extends JpaRepository<VerificationToken, UUID> {

}
