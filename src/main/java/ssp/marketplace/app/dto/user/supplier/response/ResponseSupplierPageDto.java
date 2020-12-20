package ssp.marketplace.app.dto.user.supplier.response;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.user.User;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class ResponseSupplierPageDto implements Serializable {
    private UUID id;
    private String companyName;
    private String inn;
    private List<String> tags;

    public ResponseSupplierPageDto(User user) {
        id = user.getId();
        companyName = user.getSupplierDetails().getCompanyName();
        inn = user.getSupplierDetails().getInn();
        tags = user.getSupplierDetails().getTags().stream().map(Tag::getTagName).collect(Collectors.toList());
    }

    public static List<ResponseSupplierPageDto> listOfDto(Set<User> users){
        return users.stream().map(ResponseSupplierPageDto::new).collect(Collectors.toList());
    }
}
