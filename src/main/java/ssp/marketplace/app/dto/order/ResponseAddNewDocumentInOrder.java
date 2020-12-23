package ssp.marketplace.app.dto.order;

import lombok.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.DocumentService;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseAddNewDocumentInOrder {
    private List<String> name;
}