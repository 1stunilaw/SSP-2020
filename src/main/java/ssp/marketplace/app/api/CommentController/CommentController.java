package ssp.marketplace.app.api.CommentController;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{orderId}")
    public List<ResponseCommentDto> getCommentss(
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest req,
            @PathVariable UUID orderId
    ) {
        return commentService.getAll(req, pageable, orderId);
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<?> addNewQuestion(
            @RequestBody CommentDto commentDto,
            HttpServletRequest request
    ) {
        CommentDto comment = commentService.addComment(request, commentDto);
        return new ResponseEntity<>(
                comment,
                HttpStatus.CREATED);
    }

    @PostMapping("/{parentId}/addAnswer")
    public ResponseEntity<?> addNewAnswer(
            @RequestBody CommentDto commentDto,
            HttpServletRequest request,
            @PathVariable UUID parentId
    ) {
        CommentDto comment = commentService.addComment(request, commentDto, parentId);
        return new ResponseEntity<>(
                comment,
                HttpStatus.CREATED);
    }
}
