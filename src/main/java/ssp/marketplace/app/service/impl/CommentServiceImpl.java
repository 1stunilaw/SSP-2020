package ssp.marketplace.app.service.impl;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.mappers.*;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;
import ssp.marketplace.app.exceptions.NotFoundException;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OrderRepository orderRepository;

    private final CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);

    @Override
    public CommentDto addComment(HttpServletRequest request, CommentDto commentDto, UUID parentId) {
        Comment comment = newComment(request, commentDto);

        Comment question = commentRepository
                .findById(parentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        comment.setQuestion(question);

        if(question.getAccessLevel() ==CommentAccessLevel.PRIVATE){
            question.setAccessLevel(commentDto.getAccessLevel());
        }
        commentRepository.save(question);

        comment = commentRepository.save(comment);
        return mapper.commentToCommentDto(comment);
    }

    @Override
    public Page<ResponseCommentDto> getUsersComments(HttpServletRequest req, Pageable pageable) {
        User user = userService.getUserFromHttpServletRequest(req);
        return MapToDtoAndToPages(user.getComments(), pageable);
    }

//    @Override
//    public Page<ResponseCommentDto> getAllForAdmin(HttpServletRequest req, Pageable pageable) {
//
//        User user = userService.getUserFromHttpServletRequest(req);
//
//
//        return null;
//    }

    @Override
    public CommentDto addComment(HttpServletRequest request, CommentDto commentDto) {
        Comment comment = newComment(request, commentDto);
        comment = commentRepository.save(comment);

        return mapper.commentToCommentDto(comment);
    }

    private Comment newComment(HttpServletRequest request, CommentDto commentDto) {
        User user  = userService.getUserFromHttpServletRequest(request);

        Comment comment = new Comment();
        comment.setStatus(StatusForComment.ACTIVE);
        comment.setAccessLevel(CommentAccessLevel.PRIVATE);
        comment.setUser(user);
        comment.setText(commentDto.getText());

        UUID orderId = commentDto.getOrderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        comment.setOrder(order);

        return comment;
    }

    @Override
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий не найденd"));
        comment.setStatus(StatusForComment.DELETED);

        commentRepository.save(comment);
    }

    @Override
    public Page<ResponseCommentDto> getAll(HttpServletRequest request, Pageable page, UUID orderId) {
        String token = jwtTokenProvider.resolveToken(request);
        List<String> roles = jwtTokenProvider.getRole(token);

        return roles.contains(RoleName.ROLE_ADMIN.toString())
                ? getAllForAdmin(page, orderId)
                : getAllForUser(request, page, orderId);
    }

    private Page<ResponseCommentDto> getAllForUser(HttpServletRequest request, Pageable pageable, UUID orderId) {
        User user  = userService.getUserFromHttpServletRequest(request);
        return MapToDtoAndToPages(commentRepository.findAllByOrderIdAndUserId(orderId, user.getId()), pageable);
    }

    private Page<ResponseCommentDto> getAllForAdmin(Pageable pageable, UUID orderId) {
        return MapToDtoAndToPages(commentRepository.findByOrderId(orderId), pageable);
    }

    private Page<ResponseCommentDto> MapToDtoAndToPages(List<Comment> comments, Pageable pageable)
    {
        ResponseCommentDtoMapper responseCommentDtoMapper = Mappers.getMapper(ResponseCommentDtoMapper.class);
        List<ResponseCommentDto> responseCommentDtoList = responseCommentDtoMapper.createDtoFromComments(comments);
        if(responseCommentDtoList.isEmpty()) throw new NotFoundException("К заказу нет комментариев");

        long start = pageable.getOffset();
        long end = start + pageable.getPageSize() > responseCommentDtoList.size()
                ? responseCommentDtoList.size()
                : start + pageable.getPageSize();

        Page<ResponseCommentDto> pages = new PageImpl<ResponseCommentDto>(
                responseCommentDtoList.subList((int)start, (int)end), pageable, responseCommentDtoList.size());
        return pages;
    }
}
