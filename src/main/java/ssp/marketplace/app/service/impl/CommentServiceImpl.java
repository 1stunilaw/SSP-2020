package ssp.marketplace.app.service.impl;

import lombok.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.mappers.*;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForComment;
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

    @Override
    public CommentDto addComment(HttpServletRequest request, CommentDto commentDto, UUID parentId) {
        Comment comment = newComment(request, commentDto);
        comment.setQuestion(commentRepository
                .findById(parentId)
                .orElseThrow(() -> new NotFoundException(Comment.class.getName(), parentId)));
        comment = commentRepository.save(comment);

        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);

        return mapper.commentToCommentDto(comment);
    }

    @Override
    public CommentDto addComment(HttpServletRequest request, CommentDto commentDto) {
        Comment comment = newComment(request, commentDto);
        comment = commentRepository.save(comment);

        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);

        return mapper.commentToCommentDto(comment);
    }

    public Comment newComment(HttpServletRequest request, CommentDto commentDto) {
        Comment comment = new Comment();
        User user  = userService.getUserFromHttpServletRequest(request);
        comment.setStatus(StatusForComment.ACTIVE);
        comment.setUser(user);
        comment.setText(commentDto.getText());
        UUID orderId = commentDto.getOrderId();
        if(!orderRepository.existsById(orderId))
        {
            throw new NotFoundException(Order.class.getName(), orderId);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class.getName(), orderId));
        comment.setOrder(order);
        return comment;
    }

    @Override
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Comment.class.getName(), id));
        comment.setStatus(StatusForComment.DELETED);

        commentRepository.save(comment);
    }

    @Override
    public List<ResponseCommentDto> getAll(HttpServletRequest request, Pageable page, UUID orderId) {
        String token = jwtTokenProvider.resolveToken(request);
        List<String> roles = jwtTokenProvider.getRole(token);

        for(String role : roles) {
            System.out.println(role);
            if(role == RoleName.ROLE_ADMIN.toString()) {
               return getAllForAdmin(page, orderId);
            }
        }
        return getAllForUser(request, page, orderId);
    }

    private List<ResponseCommentDto> getAllForUser(HttpServletRequest request, Pageable page, UUID orderId) {
        User user  = userService.getUserFromHttpServletRequest(request);
        List<Comment> comments = user.getComments();
        List<ResponseCommentDto> responseCommentDtoList= new ArrayList<>();
        CommentDtoMapper mapper = Mappers.getMapper(CommentDtoMapper.class);
        ResponseCommentDtoMapper responseCommentDtoMapper = Mappers.getMapper(ResponseCommentDtoMapper.class);

        for(Comment tempComment : comments) {
           responseCommentDtoList.add(responseCommentDtoMapper.createDtoFromComments(tempComment));
        }
        return responseCommentDtoList;
    }

    private List<ResponseCommentDto> getAllForAdmin(Pageable pageable, UUID orderId) {
        List<Comment> comments = commentRepository.findByOrderId(orderId);
        List<ResponseCommentDto> responseCommentDtoList = new ArrayList<>();

        ResponseCommentDtoMapper responseCommentDtoMapper = Mappers.getMapper(ResponseCommentDtoMapper.class);

        for(Comment tempComment : comments) {
            responseCommentDtoList.add(responseCommentDtoMapper.createDtoFromComments(tempComment));
        }
        return  responseCommentDtoList;
    }
}
