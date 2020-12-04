package ssp.marketplace.app.dto.suppliers.edit;

import lombok.*;
import ssp.marketplace.app.entity.Tag;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class EditSupplierDataRequestDto implements Serializable {
    @NotBlank
    private String inn;
    @NotBlank
    private String phone;
    @NotBlank
    private String contactFio;
    @NotBlank
    private String region;
    @NotBlank
    private UUID lawStatusId;
    @NotEmpty
    private Set<UUID> tags;
}
