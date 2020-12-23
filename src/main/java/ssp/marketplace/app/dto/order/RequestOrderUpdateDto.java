package ssp.marketplace.app.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.service.OrderService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderUpdateDto {

    @Length(max = 250, message = "{name.errors.length.max}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{name.errors.regex}")
    private String name;

    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    @Length(max = 10000, message = "{description.errors.length}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{description.errors.regex}")
    private String description;

    private StatusForOrder statusForOrder;

    private List<UUID> tags;

    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{organizationName.errors.regex}")
    @Length(max = 250, message = "{organizationName.errors.length}")
    private String organizationName;

    private MultipartFile[] files;
}