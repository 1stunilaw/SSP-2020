package ssp.marketplace.app.dto.user.supplier;

import ssp.marketplace.app.dto.user.UserResponseDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForDocument;

import java.util.List;
import java.util.stream.Collectors;

public class SupplierResponseDtoWithNewToken extends UserResponseDto{
    private String companyName;
    private String description;
    private String inn;
    private String phone;
    private String contactFio;
    private String region;
    private String nda;
    private String lawStatus;
    private List<String> tags;
    private List<String> documents;

    private String token;

    public SupplierResponseDtoWithNewToken(User user, String token) {
        super(user);

        description = user.getSupplierDetails().getDescription();
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

        this.token = token;
    }
}
