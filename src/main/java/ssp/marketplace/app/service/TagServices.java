package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.dto.responseDto.ResponseTag;

import java.util.List;

public interface TagServices {

    List<ResponseTag> getTags();

    void addNewTag(RequestTag requestTag);
}
