package ssp.marketplace.app.entity;

import lombok.*;
import ssp.marketplace.app.entity.statuses.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "offers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer extends BasicEntity{

    /**
     * id * из BasicEntity
     * заказ
     * организация
     * описание
     * статус
     * номер
     * время х2 * из BasicEntity
     * файлы
     */

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status")
    private StatusForOffer statusForOffer;
/*
    @Column(name="number", nullable=false, unique=true, insertable = false, updatable = true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)*/

    @Column(name="number", nullable=false, unique=true, insertable = false,
            updatable = true, columnDefinition = "BIGINT DEFAULT nextval('offers_number_seq')")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "offers_number_seq")
    @SequenceGenerator(name = "offers_number_seq", sequenceName ="offers_number_seq")
    private Long number;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "offers_documents",
            joinColumns = {@JoinColumn(name = "offer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "doc_id", referencedColumnName = "id")})
    private List<Document> documents;
}
