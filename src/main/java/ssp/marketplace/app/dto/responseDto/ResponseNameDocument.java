package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseNameDocument  {

    private List<String> name;

}