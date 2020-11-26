package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.time.LocalDateTime;
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

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStop;

    private String description;

    private List<String> documents;

    private String user;
}