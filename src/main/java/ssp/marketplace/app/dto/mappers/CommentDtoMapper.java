package ssp.marketplace.app.dto.mappers;

import org.mapstruct.*;
import ssp.marketplace.app.dto.*;
import ssp.marketplace.app.entity.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target="orderId", source = "comment.order.id"),
            @Mapping(target="id", source = "comment.id")})
    CommentDto commentToCommentDto(Comment comment);
}
