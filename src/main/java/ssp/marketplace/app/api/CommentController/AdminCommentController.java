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
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    @PostMapping("/{parentId}/addAnswer")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addNewAnswer(
            @Valid @NotNull @RequestBody RequestCommentDto commentDto,
            HttpServletRequest request,
            @PathVariable UUID parentId) {
        return commentService.addComment(request, commentDto, parentId);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }

//    @GetMapping("/getAllForAdmin")
//    @ResponseStatus(HttpStatus.OK)
//    public Page<ResponseCommentDto> getComments(
//            @PageableDefault(size = 30, value = 30) Pageable pageable,
//            HttpServletRequest req,
//            @PathVariable UUID orderId) {
//        return commentService.getAllForAdmin(req, pageable, orderId);
//    }
}
