package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;
import ssp.marketplace.app.entity.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierResponseDto extends UserResponseDto {
    private String companyName;
    private String description;
    private String inn;
    private String phone;
    private String fio;
    private String contacts;
    private String region;
    private String nda;
    private String lawStatus;


    private List<String> tags;
    private List<String> documents;

    public SupplierResponseDto(User user) {
        super(user);

        description = user.getSupplierDetails().getDescription();
        companyName = user.getSupplierDetails().getCompanyName();
        inn = user.getSupplierDetails().getInn();
        phone = user.getSupplierDetails().getPhone();
        fio = user.getSupplierDetails().getContactFio();
        contacts = user.getSupplierDetails().getContacts();
        region = user.getSupplierDetails().getRegion();
        nda = user.getSupplierDetails().getNda();
        if(user.getSupplierDetails().getLawStatus() != null){
            lawStatus = user.getSupplierDetails().getLawStatus().getName();
        }
        tags = user.getSupplierDetails().getTags().stream().map(Tag::getTagName).collect(Collectors.toList());
        documents = user.getSupplierDetails().getDocuments().stream()
                .filter(doc -> doc.getStatusForDocument().equals(StatusForDocument.ACTIVE))
                .map(Document::getName).collect(Collectors.toList());
    }
}
