package ssp.marketplace.app.dto.tag;

import lombok.*;

import javax.validation.constraints.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeleteTags {

    @NotNull(message = "{tagsId.errors.empty}")
    @Size(min = 1, message = "{tagsId.errors.empty}")
    private Set<@NotNull(message = "{tagsId.errors.empty}") UUID> tagsId;
}
