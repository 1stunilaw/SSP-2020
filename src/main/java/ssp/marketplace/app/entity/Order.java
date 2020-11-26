package ssp.marketplace.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_stop", nullable = false)
    private LocalDateTime dateStop;

    @Column(name = "description")
    private String description;

//    @JsonProperty("number")
    @Column(name="number", nullable=false, unique=true, insertable = false,
            updatable = true, columnDefinition = "BIGINT DEFAULT nextval('orders_number_seq')")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "orders_number_seq")
    @SequenceGenerator(name = "orders_number_seq", sequenceName ="orders_number_seq")
    private Long number;

    @Column(name = "organization_name")
    private String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusForOrder statusForOrder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_tags",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private List<Tag> tags;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_documents",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "doc_id", referencedColumnName = "id")})
    private List<Document> documents;
}

