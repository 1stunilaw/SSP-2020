package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderUpdateDto {

    private String name;

    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    private String description;

    private StatusForOrder statusForOrder;

    private List<String> tags;

    private String organizationName;

    private List<String> documents;

    private MultipartFile[] files;
}