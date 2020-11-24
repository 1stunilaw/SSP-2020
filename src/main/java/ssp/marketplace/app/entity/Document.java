package ssp.marketplace.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ssp.marketplace.app.entity.statuses.StatusForDocument;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "documents")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document extends BasicEntity{

    @Column(name = "name_document", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusForDocument statusForDocument;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "order_id")
//    @JsonBackReference
//    private Order order;

    @ManyToMany(mappedBy = "documents", fetch = FetchType.LAZY)
    private List<Order> ordersList;
}
