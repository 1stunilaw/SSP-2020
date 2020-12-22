package ssp.marketplace.app.api.CommentController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.responseDto.ResponseOneCommentDto;
import ssp.marketplace.app.dto.requestDto.RequestCommentDto;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Контроллер для работы с комментариями, которую может выполнять только ADMIN
 */
@AllArgsConstructor
@RestController
@RequestMapping("api/admin/comments")
public class AdminCommentController {

    private final CommentService commentService;

    /**
     * Запрос на создание ответа на комментарий для заказчика
     * @param parentId - id вопроса
     */
    @PostMapping("/{parentId}/addAnswer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOneCommentDto addNewAnswer(
            @Valid @NotNull @RequestBody RequestCommentDto commentDto,
            HttpServletRequest request,
            @PathVariable String parentId) {
        try {
            UUID id = UUID.fromString(parentId);
            return commentService.addComment(request, commentDto, id);
        }
        catch (IllegalArgumentException ex)
        {
            throw new BadRequestException("Невалидный ID вопроса");
        }
    }

    /**
     * Запрос на удаление комментария по id
     */
    @DeleteMapping("/deleteComment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }


}
