package ssp.marketplace.app.dto.mappers;

import org.mapstruct.*;
import ssp.marketplace.app.dto.user.customer.CustomerUpdateRequestDto;
import ssp.marketplace.app.entity.user.User;

public interface CustomerUpdateDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(CustomerUpdateRequestDto dto, @MappingTarget User user);

}
