package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.BadRequestException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderDto {

    @NotBlank(message = "{name.errors.empty}")
    @NotNull(message = "{name.errors.empty}")
    @Size(max = 250)
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

    private List<UUID> tags;

    private String organizationName;

    private MultipartFile[] files;
}