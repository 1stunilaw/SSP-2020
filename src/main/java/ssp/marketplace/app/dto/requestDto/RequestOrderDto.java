package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderDto {

    @NotBlank(message = "{name.errors.empty}")
    @NotNull(message = "{name.errors.empty}")
    @Size(max = 1000)
    private String name;
    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @NotNull(message = "{dateStop.errors.empty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    @NotBlank(message = "{description.errors.empty}")
    @Size(max = 10000)
    private String description;

    private StatusForOrder statusForOrder;

    private List<String> tags;

    private String organizationName;

    private MultipartFile[] files;
}