package ssp.marketplace.app.dto.mappers;

import org.mapstruct.*;
import ssp.marketplace.app.dto.RegisterUserDto;
import ssp.marketplace.app.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void createUserFromDto(RegisterUserDto dto, @MappingTarget User user);
}
