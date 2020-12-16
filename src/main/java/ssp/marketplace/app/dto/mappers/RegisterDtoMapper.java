package ssp.marketplace.app.dto.mappers;

import org.mapstruct.*;
import ssp.marketplace.app.dto.registration.RegisterRequestUserDto;
import ssp.marketplace.app.entity.user.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void createUserFromDto(RegisterRequestUserDto dto, @MappingTarget User user);
}
