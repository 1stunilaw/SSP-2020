package ssp.marketplace.app.service.impl;

import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.entity.Tag;
import ssp.marketplace.app.repository.TagRepository;
import ssp.marketplace.app.service.TagServices;

@Service
public class TagServicesImpl implements TagServices {

    private final TagRepository tagRepository;

    public TagServicesImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void addNewTag(RequestTag requestTag) {
        Tag tag = new Tag();
        tag.setTagName(requestTag.getTagName());
        tagRepository.save(tag);
    }
}
