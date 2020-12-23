package ssp.marketplace.app.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.responseDto.ResponseOneCommentDto;
import ssp.marketplace.app.dto.requestDto.RequestCommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import java.util.*;

public interface CommentService {

    ResponseOneCommentDto addComment(HttpServletRequest request, RequestCommentDto commentDto);

    void deleteComment(UUID id);

    Page<ResponseCommentDto> getAll(HttpServletRequest request, Pageable page, UUID orderId);

    ResponseOneCommentDto addComment(HttpServletRequest request, RequestCommentDto commentDto, UUID parentId);


//    Page<ResponseCommentDto> getUsersComments(HttpServletRequest req, Pageable pageable);

//    Page<ResponseCommentDto> getAllForAdmin(HttpServletRequest req, Pageable pageable);
}
