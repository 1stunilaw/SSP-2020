package ssp.marketplace.app.entity.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.lang.Nullable;
import ssp.marketplace.app.entity.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "supplier_details")
public class SupplierDetails extends BasicEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    @Column(name = "description", length = 2500)
    private String description;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "nda", length = 50)
    private String nda;

    @Column(name = "contacts", length = 500)
    private String contacts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "suppliers_documents",
            joinColumns = {@JoinColumn(name = "supplier_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "doc_id", referencedColumnName = "id")})
    private List<Document> documents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "suppliers_tags",
            joinColumns = {@JoinColumn(name = "supplier_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private Set<Tag> tags;


    @Nullable
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="law_status_id")
    private LawStatus lawStatus;

    /**
     * Конструктор данных поставщика при регистрации
     * @param user пользователь-поставщик
     * @param companyName название компании
     */
    public SupplierDetails(User user, String companyName) {
        super();
        this.user = user;
        this.companyName = companyName;
    }
}
