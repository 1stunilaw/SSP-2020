package ssp.marketplace.app.entity.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ssp.marketplace.app.entity.BasicEntity;
import ssp.marketplace.app.entity.statuses.StateStatus;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "law_statuses")
@NoArgsConstructor
public class LawStatus extends BasicEntity {
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lawStatus", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<SupplierDetails> supplier;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StateStatus status = StateStatus.ACTIVE;

    public LawStatus(String name) {
        super();
        this.name = name;
    }
}
