package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.dto.CommentDto;

import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
public class ResponseCommentDto implements Comparable<ResponseCommentDto>{
    CommentDto question;
    List<CommentDto> answers;
    String creationDate;

    @Override
    public int compareTo(ResponseCommentDto o) {
        return getCreationDate().compareTo(o.creationDate);
    }
}
