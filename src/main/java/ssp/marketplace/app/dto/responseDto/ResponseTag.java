package ssp.marketplace.app.dto.responseDto;

import lombok.*;
import ssp.marketplace.app.entity.Tag;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTag {

    private UUID id;

    private String tagName;

    public static ResponseTag getResponseTagFromTag(Tag tag){
        ResponseTag responseTag = builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .build();
        return responseTag;
    }
}
