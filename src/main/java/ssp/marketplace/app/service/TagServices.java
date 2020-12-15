package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.ResponseTag;

import java.util.*;

public interface TagServices {

    List<ResponseTag> getTags();

    List<ResponseTag> addNewTag(RequestTag requestTag);

    void deleteTag(UUID id);

    ResponseTag editTag(UUID id, RequestUpdateTag requestUpdateTag);
}
