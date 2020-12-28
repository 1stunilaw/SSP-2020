package ssp.marketplace.app.entity;

import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Generated;
import ssp.marketplace.app.entity.statuses.*;
import ssp.marketplace.app.entity.user.User;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "offers")
@Getter
@Setter
@RequiredArgsConstructor
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status")
    private StateStatus stateStatus;

    //@Generated(GenerationTime.INSERT)
    private Long number;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "offers_documents",
            joinColumns = {@JoinColumn(name = "offer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "doc_id", referencedColumnName = "id")})
    private List<Document> documents;
}
