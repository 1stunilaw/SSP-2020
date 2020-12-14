package ssp.marketplace.app.entity.supplier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ssp.marketplace.app.entity.BasicEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "law_statuses")
public class LawStatus extends BasicEntity {
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "lawStatus")
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private SupplierDetails supplier;
}
