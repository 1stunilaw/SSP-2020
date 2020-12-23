package ssp.marketplace.app.entity;

import lombok.*;
import ssp.marketplace.app.entity.statuses.StateStatus;

import javax.persistence.*;

@Entity
@Table(name = "documents")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document extends BasicEntity {

    @Column(name = "name_document", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StateStatus statusForDocument;
}
