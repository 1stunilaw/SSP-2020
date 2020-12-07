package ssp.marketplace.app.dto.user.supplier;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ssp.marketplace.app.entity.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class SupplierPageResponseDto implements Serializable {
    private UUID id;
    private String companyName;
    private String inn;
    private List<String> tags;

    public SupplierPageResponseDto(User user) {
        id = user.getId();
        companyName = user.getSupplierDetails().getCompanyName();
        inn = user.getSupplierDetails().getInn();
        tags = user.getSupplierDetails().getTags().stream().map(Tag::getTagName).collect(Collectors.toList());
    }

    public static List<SupplierPageResponseDto> listOfDto(Set<User> users){
        return users.stream().map(x -> {
            log.info(x.getEmail());
            log.info(x.getSupplierDetails() + " in listOfDto");
            return new SupplierPageResponseDto(x);
        }).collect(Collectors.toList());
    }
}
