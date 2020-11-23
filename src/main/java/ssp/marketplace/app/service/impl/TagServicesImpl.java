package ssp.marketplace.app.service.impl;

import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.entity.Tag;
import ssp.marketplace.app.repository.TagRepository;
import ssp.marketplace.app.service.TagServices;

import java.util.List;

@Service
public class TagServicesImpl implements TagServices {

    private final TagRepository tagRepository;

    public TagServicesImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void addNewTag(RequestTag requestTag) {

        List<String> tagName = requestTag.getTagName();
        for (String t: tagName
             ) {
            Tag tag = new Tag();
            tag.setTagName(t);
            tagRepository.save(tag);
        }
    }
}
