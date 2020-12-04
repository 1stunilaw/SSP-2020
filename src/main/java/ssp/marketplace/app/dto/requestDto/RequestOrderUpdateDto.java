package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.BadRequestException;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestOrderUpdateDto {

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

    private List<String> documents;

    public static RequestOrderUpdateDto convert(String requestOrderDto) {
        if (requestOrderDto == null) {
            throw new BadRequestException("order не может быть пустым");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        RequestOrderUpdateDto dtoObject;
        try {
            dtoObject = mapper.readValue(requestOrderDto, RequestOrderUpdateDto.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Ключи для полей заполнены неверно");
        }
        return dtoObject;
    }
}