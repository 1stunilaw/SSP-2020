package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;
import ssp.marketplace.app.entity.supplier.LawStatus;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class SupplierResponseDto extends UserResponseDto {
    private String companyName;
    private String inn;
    private String phone;
    private String contactFio;
    private String region;
    private String nda;
    private String lawStatus;


    private List<String> tags;
    private List<String> documents;

    public SupplierResponseDto(User user) {
        super(user);

        companyName = user.getSupplierDetails().getCompanyName();
        inn = user.getSupplierDetails().getInn();
        phone = user.getSupplierDetails().getPhone();
        contactFio = user.getSupplierDetails().getContactFio();
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
