package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.CommentDto;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;

import java.util.*;

public interface CommentService {

    CommentDto addComment(HttpServletRequest request, CommentDto commentDto);

    void deleteComment(UUID id);

    List<ResponseCommentDto> getAll(HttpServletRequest request, Pageable page, UUID orderId);

    CommentDto addComment(HttpServletRequest request, CommentDto commentDto, UUID parentId);
}
