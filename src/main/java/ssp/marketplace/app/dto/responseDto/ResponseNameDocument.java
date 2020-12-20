package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// TODO: 20.12.2020 Переименовать
public class ResponseNameDocument  {

    private List<String> name;

}