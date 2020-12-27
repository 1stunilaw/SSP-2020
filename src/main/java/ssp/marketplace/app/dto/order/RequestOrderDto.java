package ssp.marketplace.app.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.service.OrderService;
import ssp.marketplace.app.validation.unique.Unique;

import javax.validation.constraints.*;
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
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{name.errors.regex}")
    @Unique(message = "{order.errors.unique}", service = OrderService.class, fieldName = "name")
    private String name;
    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @NotNull(message = "{dateStop.errors.empty}")
    @Unique(message = "{dateStop.errors.before}", service = OrderService.class, fieldName = "dateStop")
    private String dateStop;

    @NotBlank(message = "{description.errors.empty}")
    @Length(min = 1, max = 10000, message = "{description.errors.length}")
    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{description.errors.regex}")
    private String description;

    @Pattern(regexp = "^($|WAITING_OFFERS|CONSIDERATION|CLOSED|DELETED|\\s)", message = "{statusForOrder.errors.value}")
    private String statusForOrder;

    private List<UUID> tags;

    @Pattern(regexp = "(^$)|(^[а-яА-ЯёЁa-zA-Z0-9-a-zA-ZА-я-()<>@#№$;%*_=^/{}\\[\\].,!?':\\s+&\" ]+$)", message = "{organizationName.errors.regex}")
    @Length(max = 250, message = "{organizationName.errors.length}")
    private String organizationName;

    private MultipartFile[] files;
}