package ssp.marketplace.app.service.impl;

import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.dto.responseDto.ResponseTag;
import ssp.marketplace.app.entity.Tag;
import ssp.marketplace.app.exceptions.BadRequest;
import ssp.marketplace.app.repository.TagRepository;
import ssp.marketplace.app.service.TagServices;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServicesImpl implements TagServices {

    private final TagRepository tagRepository;

    public TagServicesImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<ResponseTag> getTags() {
        List<Tag> allTags = tagRepository.findAll();
        List<ResponseTag> responseTagStream =
                allTags.stream().map(ResponseTag::getResponseTagFromTag).collect(Collectors.toList());
        return responseTagStream;
    }

    @Override
    public void addNewTag(RequestTag requestTag) {
        List<String> tagName = requestTag.getTagName();
        for (String name : tagName
        ) {
            if (tagRepository.findByTagName(name).isPresent()) {
                throw new BadRequest("Тег с именем " + name + " уже существует");
            }
        }
        for (String t : tagName
        ) {
            Tag tag = new Tag();
            tag.setTagName(t);
            tagRepository.save(tag);
        }
    }
}
