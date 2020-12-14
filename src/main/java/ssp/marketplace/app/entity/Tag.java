package ssp.marketplace.app.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ssp.marketplace.app.entity.supplier.SupplierDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tags")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Order> ordersList;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<SupplierDetails> suppliers;

}
