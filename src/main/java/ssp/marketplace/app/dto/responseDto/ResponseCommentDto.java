package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.dto.CommentDto;

import java.util.*;

@Getter
@Setter
public class ResponseCommentDto {
    CommentDto question;
    List<CommentDto> answers;
}
