package ssp.marketplace.app.dto.mappers;

import org.mapstruct.*;
import ssp.marketplace.app.dto.responseDto.ResponseOneCommentDto;
import ssp.marketplace.app.entity.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentDtoMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target="userId", source = "comment.user.id"),
            @Mapping(target="orderId", source = "comment.order.id"),
            @Mapping(target="id", source = "comment.id")})
    ResponseOneCommentDto commentToCommentDto(Comment comment);
}
