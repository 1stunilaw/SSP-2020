package ssp.marketplace.app.dto.requestDto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTag {

    private List<String> tagName;
}
