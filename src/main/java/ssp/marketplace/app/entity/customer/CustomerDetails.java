package ssp.marketplace.app.entity.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ssp.marketplace.app.entity.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class CustomerDetails extends BasicEntity {

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "fio", length = 150)
    private String fio;

    @Column(name = "phone", length = 20)
    private String phone;

    public CustomerDetails(User user, String fio, String phone) {
        this.user = user;
        this.fio = fio;
        this.phone = phone;
    }
}
