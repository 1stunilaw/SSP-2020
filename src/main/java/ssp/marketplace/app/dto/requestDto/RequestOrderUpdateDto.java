package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.BadRequestException;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderUpdateDto {

    @Size(max = 250)
    private String name;

    //    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
    //    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStop;

    @Size(max = 10000)
    private String description;

    private StatusForOrder statusForOrder;

    private List<UUID> tags;

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

    private MultipartFile[] files;

}