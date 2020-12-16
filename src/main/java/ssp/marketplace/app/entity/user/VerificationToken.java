package ssp.marketplace.app.entity.user;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ssp.marketplace.app.entity.user.User;

import javax.persistence.*;
import java.sql.*;
import java.util.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "verification")
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;


    public VerificationToken(User user) {
        this.user = user;
        expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Timestamp calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    public void setExpiryDate(final Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

}
