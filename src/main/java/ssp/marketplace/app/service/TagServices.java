package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.dto.responseDto.ResponseTag;

import java.util.*;

public interface TagServices {

    List<ResponseTag> getTags();

    void addNewTag(RequestTag requestTag);

    void deleteTag(UUID id);

    void editTag(UUID id);
}
