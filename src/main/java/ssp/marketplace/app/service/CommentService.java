package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.CommentDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.requestDto.RequestCommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import java.util.*;

public interface CommentService {

    CommentDto addComment(HttpServletRequest request, RequestCommentDto commentDto);

    void deleteComment(UUID id);

    Page<ResponseCommentDto> getAll(HttpServletRequest request, Pageable page, UUID orderId);

    CommentDto addComment(HttpServletRequest request, RequestCommentDto commentDto, UUID parentId);

    // TODO: 20.12.2020 Разобраться с необходимостью методов на данном этапе
    Page<ResponseCommentDto> getUsersComments(HttpServletRequest req, Pageable pageable);

//    Page<ResponseCommentDto> getAllForAdmin(HttpServletRequest req, Pageable pageable);
}
