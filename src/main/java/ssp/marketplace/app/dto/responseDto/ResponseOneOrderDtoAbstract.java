package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ResponseOneOrderDtoAbstract {

    private UUID id;

    private String name;

    private Long number;

    private List<String> tags;

    private StatusForOrder statusForOrder;

    private String dateStart;

    private String dateStop;

    private String description;

    private List<String> documents;

    private String user;
}