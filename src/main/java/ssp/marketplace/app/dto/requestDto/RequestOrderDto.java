package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.BadRequest;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrderDto {

    private String name;
    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    private String description;

    private StatusForOrder statusForOrder;

    private List<String> tags;

    private String organizationName;

    public static RequestOrderDto convert(String requestOrderDto) {
        if (requestOrderDto == null) {
            throw new BadRequest("order не может быть пустым");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        RequestOrderDto dtoObject;
        try {
            dtoObject = mapper.readValue(requestOrderDto, RequestOrderDto.class);
        } catch (JsonProcessingException e) {
            throw new BadRequest("Передаваемые знчения не соответствуют формату");
        }

        if (dtoObject.name == null) {
            throw new BadRequest("Имя не может быть пустым");
        }
        if (dtoObject.description == null) {
            throw new BadRequest("Описание не может быть пустым");
        }
        if (dtoObject.dateStop == null) {
            throw new BadRequest("Дата не может быть пустой");
        }
        return dtoObject;
    }
}