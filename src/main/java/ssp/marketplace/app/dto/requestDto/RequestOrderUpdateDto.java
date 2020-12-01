package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderUpdateDto {

    @Size(max = 250)
    private String name;

    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    @Size(max = 10000)
    private String description;

    private StatusForOrder statusForOrder;

    private List<UUID> tags;

    private String organizationName;

    private List<String> documents;

    private MultipartFile[] files;
}