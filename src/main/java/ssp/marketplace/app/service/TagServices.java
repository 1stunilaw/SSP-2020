package ssp.marketplace.app.service;

import ssp.marketplace.app.dto.requestDto.RequestTag;

import java.util.List;

public interface TagServices {

    List<String> getTags();

    void addNewTag(RequestTag requestTag);
}
