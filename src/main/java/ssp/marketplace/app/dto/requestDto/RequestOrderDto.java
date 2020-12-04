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

    public static RequestOrderDto convert(String requestOrderDto) {
        if (requestOrderDto == null) {
            throw new BadRequestException("order не может быть пустым");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        RequestOrderDto dtoObject;
        try {
            dtoObject = mapper.readValue(requestOrderDto, RequestOrderDto.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Передаваемые знчения не соответствуют формату");
        }
        if (dtoObject.name == null) {
            throw new BadRequestException("Имя не может быть пустым");
        }
        if (dtoObject.description == null) {
            throw new BadRequestException("Описание не может быть пустым");
        }
        if (dtoObject.dateStop == null) {
            throw new BadRequestException("Дата не может быть пустой");
        }
        return dtoObject;
    }

    private MultipartFile[] files;

}