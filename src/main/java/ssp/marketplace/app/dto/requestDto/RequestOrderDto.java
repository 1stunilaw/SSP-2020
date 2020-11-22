package ssp.marketplace.app.dto.requestDto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestOrderDto {

    private String name;

//    @DateTimeFormat(pattern = "YYYY-MM-dd hh:mm")
//    private LocalDateTime dateStart;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
//    @JsonFormat(locale = "hu", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "CET")
    private LocalDate dateStop;

    private String description;

    private StatusForOrder statusForOrder;

    private List<String> tags;

    private String organizationName;


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}