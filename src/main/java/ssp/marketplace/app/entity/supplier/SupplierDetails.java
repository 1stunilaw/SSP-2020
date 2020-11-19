package ssp.marketplace.app.entity.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ssp.marketplace.app.entity.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "supplier_details")
public class SupplierDetails extends BasicEntity {

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "inn", length = 20)
    private String inn;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "contact_fio", length = 150)
    private String contactFio;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "nda", length = 50)
    private String nda;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name="law_status_id")
    private LawStatus lawStatus;

    /**
     * Конструктор данных поставщика при регистрации
     * @param user пользователь-поставщик
     * @param companyName название компании
     */
    public SupplierDetails(User user, String companyName) {
        this.user = user;
        this.companyName = companyName;
    }
}
