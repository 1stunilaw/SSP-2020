package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

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
    @Length(min = 1, max = 250, message = "{name.errors.length}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{name.errors.regex}")
    private String name;
    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @NotNull(message = "{dateStop.errors.empty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    @NotBlank(message = "{description.errors.empty}")
    @Length(min = 1, max = 250, message = "{description.errors.length}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{description.errors.regex}")
    private String description;

    private StatusForOrder statusForOrder;

    private List<UUID> tags;

    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{organizationName.errors.regex}")
    private String organizationName;

    private MultipartFile[] files;
}