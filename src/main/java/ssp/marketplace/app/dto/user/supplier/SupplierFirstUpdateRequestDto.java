package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.user.UserUpdateRequestDto;
import ssp.marketplace.app.validation.ValidUUID;

import javax.validation.constraints.*;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierFirstUpdateRequestDto extends UserUpdateRequestDto {
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9\"][a-zA-ZА-я0-9-.,&\" ]+$", message = "{companyName.errors.regex}")
    private String companyName;

    @NotBlank(message = "{inn.errors.empty}")
    @Pattern(regexp = "^[\\d+]{10,12}$", message = "{inn.errors.regex}")
    private String inn;

    @NotBlank(message = "{description.errors.empty}")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9\"()][a-zA-ZА-я0-9-.,&\":_%$@#() ]+$", message = "{companyName.errors.regex}")
    private String description;

    @NotBlank(message = "{phone.errors.empty}")
    @Length(min = 6, max = 20, message = "{phone.errors.length}")
    @Pattern(regexp = "^((8|\\+[0-9]{1,3})[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{6,15}$", message = "{phone.errors.regex}")
    private String phone;

    @NotBlank(message = "{fio.errors.empty}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{fio.errors.regex}")
    @Length(min = 5, max = 150, message = "{fio.errors.length}")
    private String contactFio;

    @NotBlank(message = "{region.errors.empty}")
    @Pattern(regexp = "^[a-zA-ZА-я][a-zA-ZА-я-.\" ]+$", message = "{region.errors.regex}")
    @Length(min = 5, max = 100, message = "{region.errors.length}")
    private String region;

    @NotBlank(message = "{lawStatus.errors.empty}")
    @ValidUUID
    private String lawStatusId;

    @NotEmpty(message = "{tags.errors.empty}")
    private Set<UUID> tags;

    @NotBlank(message = "{contacts.errors.empty}")
    @Length(min = 5, max = 500, message = "{contacts.errors.length}")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9][a-zA-ZА-я0-9-.,&\" ]+$", message = "{contacts.errors.regex}")
    private String contacts;

    @Size(max = 10, message = "{files.errors.amount}")
    private MultipartFile[] files;
}
