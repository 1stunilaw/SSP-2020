package ssp.marketplace.app.dto.mappers;

import org.mapstruct.*;
import ssp.marketplace.app.dto.user.customer.RequestCustomerUpdateDto;
import ssp.marketplace.app.entity.user.User;

public interface CustomerUpdateDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(RequestCustomerUpdateDto dto, @MappingTarget User user);

}
