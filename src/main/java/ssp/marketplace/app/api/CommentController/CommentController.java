package ssp.marketplace.app.api.CommentController;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.CommentDto;
import ssp.marketplace.app.dto.requestDto.RequestCommentDto;
import ssp.marketplace.app.dto.responseDto.ResponseCommentDto;
import ssp.marketplace.app.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;


@AllArgsConstructor
@RestController
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<ResponseCommentDto> getComments(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            HttpServletRequest req,
            @PathVariable UUID orderId) {

        Pageable pageable = PageRequest.of(
                page.orElse(0),
                size.orElse(10)
        );
        return commentService.getAll(req, pageable, orderId);
    }

    @PostMapping("/addQuestion")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addNewQuestion(
            @Valid @RequestBody RequestCommentDto commentDto,
            HttpServletRequest request) {
        return commentService.addComment(request, commentDto);
    }

    @GetMapping("/getAllForUser")
    @ResponseStatus(HttpStatus.OK)
    public Page<ResponseCommentDto> getCommentsForUser(
            @PageableDefault(sort = "creationDate", size = 10, direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest req) {
        return commentService.getUsersComments(req, pageable);
    }

}