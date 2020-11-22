package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssp.marketplace.app.entity.Document;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderDto {

    private String name;

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    private LocalDateTime dateStop;

    private String description;

    private StatusForOrder statusForOrder;

    private List<String> tags;
}
