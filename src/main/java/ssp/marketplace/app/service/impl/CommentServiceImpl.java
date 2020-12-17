package ssp.marketplace.app.service.impl;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.mappers.*;
import ssp.marketplace.app.dto.requestDto.RequestCommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.exceptions.*;
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
    public CommentDto addComment(HttpServletRequest request, RequestCommentDto commentDto, UUID parentId) {
        Comment comment = newComment(request, commentDto);
        if(commentDto.getAccessLevel()==CommentAccessLevel.PUBLIC) comment.setAccessLevel(commentDto.getAccessLevel());

        Comment question = commentRepository
                .findById(parentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));

        if(question.getStatus() == StatusForComment.DELETED) throw new BadRequestException("Комментарий был удален");

        if(question.getAccessLevel() ==CommentAccessLevel.PRIVATE){
            question.setAccessLevel(comment.getAccessLevel());
        }
        comment.setQuestion(question);
        commentRepository.save(question);

        comment = commentRepository.save(comment);
        return mapper.commentToCommentDto(comment);
    }

    @Override
    public Page<ResponseCommentDto> getUsersComments(HttpServletRequest req, Pageable pageable) {
        User user = userService.getUserFromHttpServletRequest(req);
        ResponseCommentDtoMapper responseCommentDtoMapper = Mappers.getMapper(ResponseCommentDtoMapper.class);
        List<ResponseCommentDto> responseCommentDtoList = responseCommentDtoMapper.createDtoFromComments(user.getComments());
        return mapToDtoAndToPages(responseCommentDtoList, pageable);
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
    public CommentDto addComment(HttpServletRequest request, RequestCommentDto commentDto) {
        Comment comment = newComment(request, commentDto);
        comment = commentRepository.save(comment);

        return mapper.commentToCommentDto(comment);
    }

    private Comment newComment(HttpServletRequest request, RequestCommentDto commentDto) {
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
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
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
        ResponseCommentDtoMapper responseCommentDtoMapper = Mappers.getMapper(ResponseCommentDtoMapper.class);
        User user  = userService.getUserFromHttpServletRequest(request);
        List<Comment> lpublic = commentRepository.findAllByOrderIdPublic(orderId);
        List<Comment> lprivate = commentRepository.findAllByOrderIdAndUserId(orderId, user.getId());
        List<ResponseCommentDto> comments = responseCommentDtoMapper.createDtoFromPublicComments(lpublic, user.getId());
        comments.addAll(responseCommentDtoMapper.createDtoFromComments(lprivate));
        return mapToDtoAndToPages(comments, pageable);

    }

    private Page<ResponseCommentDto> getAllForAdmin(Pageable pageable, UUID orderId) {
        List<Comment> comments = commentRepository.findByOrderId(orderId);
        ResponseCommentDtoMapper responseCommentDtoMapper = Mappers.getMapper(ResponseCommentDtoMapper.class);
        List<ResponseCommentDto> responseCommentDtoList = responseCommentDtoMapper.createDtoFromComments(comments);
        return mapToDtoAndToPages(responseCommentDtoList, pageable);
    }

    private Page<ResponseCommentDto> mapToDtoAndToPages(List<ResponseCommentDto> responseCommentDtoList, Pageable pageable)
    {
        Collections.sort(responseCommentDtoList, Collections.reverseOrder());
        if(responseCommentDtoList.isEmpty()) throw new NotFoundException("К заказу нет комментариев");

        long start = pageable.getOffset();
        long end = start + pageable.getPageSize() > responseCommentDtoList.size()
                ? responseCommentDtoList.size()
                : start + pageable.getPageSize();
        if(end<start) throw new BadRequestException("Страницы не существует");

        Page<ResponseCommentDto> pages = new PageImpl<ResponseCommentDto>(
                responseCommentDtoList.subList((int)start, (int)end), pageable, responseCommentDtoList.size());
        return pages;
    }
}
