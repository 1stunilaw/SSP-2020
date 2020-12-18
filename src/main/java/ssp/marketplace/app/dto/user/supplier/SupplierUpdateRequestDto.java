package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.user.UserUpdateRequestDto;
import ssp.marketplace.app.service.UserService;
import ssp.marketplace.app.validation.unique.Unique;
import ssp.marketplace.app.validation.uuid.*;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
public class SupplierUpdateRequestDto extends UserUpdateRequestDto{
    @Unique(message = "{companyName.errors.unique}", service = UserService.class, fieldName = "companyName")
    @Length(min = 2, max = 255, message = "companyName.errors.length")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9\"][a-zA-ZА-я0-9-.,&\" ]+$", message = "{companyName.errors.regex}")
    private String companyName;

    @Length(min = 5, max = 10000, message = "{description.errors.length}")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9\"()][a-zA-ZА-я0-9-.,&\":_%$@#() ]+$", message = "{companyName.errors.regex}")
    private String description;

    @Pattern(regexp = "^(\\d{10}|\\d{12})$", message = "{inn.errors.regex}")
    @Unique(message = "{inn.errors.unique}", service = UserService.class, fieldName = "inn")
    private String inn;

    @Unique(message = "{phone.errors.unique}", service = UserService.class, fieldName = "supplierPhone")
    @Length(min = 6, max = 20, message = "{phone.errors.length}")
    @Pattern(regexp = "^((8|\\+[0-9]{1,3})[\\-]?)?(\\(?\\d{3}\\)?[\\-]?)?[\\d\\-]{6,15}$", message = "{phone.errors.regex}")
    private String phone;

    @Pattern(regexp = "^[a-zA-ZА-я\"][a-zA-ZА-я-.\" ]+$", message = "{fio.errors.regex}")
    @Length(min = 5, max = 150, message = "{fio.errors.length}")
    private String fio;

    @Pattern(regexp = "^[a-zA-ZА-я0-9\"()][a-zA-ZА-я-.,\":()# ]+$", message = "{region.errors.regex}")
    @Length(min = 5, max = 100, message = "{region.errors.length}")
    private String region;

    @Length(min = 5, max = 500, message = "{contacts.errors.length}")
    @Pattern(regexp = "^(?!^\\d+$)[a-zA-ZА-я0-9\"()+@#][a-zA-ZА-я0-9-.,&\":_%$+@#() ]+$", message = "{contacts.errors.regex}")
    private String contacts;

    @ValidUUID(message = "{uuid.errors.regex}")
    private String lawStatusId;

    @CollectionOfUiid(message = "{uuid.errors.regex}")
    private Set<String> tags;

    @Size(max = 10, message = "{files.errors.amount}")
    private MultipartFile[] files;

    @Pattern(regexp = "^(true|false)$", message = "{sendMail.errors.regex}")
    private String sendEmail;
}
