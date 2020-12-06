package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.BadRequestException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderUpdateDto {

    @Length(min = 1, max = 50, message = "{name.errors.length}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{name.errors.regex}")
    private String name;

    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    @Length(max = 10000, message = "{description.errors.length}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{description.errors.regex}")
    private String description;

    private StatusForOrder statusForOrder;

    private List<UUID> tags;

    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{organizationName.errors.regex}")
    private String organizationName;

    private List<String> documents;

    private MultipartFile[] files;

}