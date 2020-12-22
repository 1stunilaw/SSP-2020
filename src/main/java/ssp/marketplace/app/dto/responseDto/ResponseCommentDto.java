package ssp.marketplace.app.dto.responseDto;

import lombok.*;

import java.util.*;

/**
 * Дто для ветки комментариев вида Вопрос- Ответы
 */
@Getter
@Setter
public class ResponseCommentDto implements Comparable<ResponseCommentDto>{
    private ResponseOneCommentDto question;
    private List<ResponseOneCommentDto> answers;

    @Override
    public int compareTo(ResponseCommentDto o) {
        return getQuestion().getCreatedAt().compareTo(o.question.getCreatedAt());
    }
}
