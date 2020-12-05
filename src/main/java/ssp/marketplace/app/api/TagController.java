package ssp.marketplace.app.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.dto.responseDto.ResponseTag;
import ssp.marketplace.app.service.TagServices;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TagController {

    private final TagServices tagServices;

    public TagController(TagServices tagServices) {
        this.tagServices = tagServices;
    }

    @PostMapping(value = "/admin/tags/add-tag")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewTag(
            @RequestBody @Valid RequestTag requestTag
    ) {
        tagServices.addNewTag(requestTag);
    }

    @GetMapping("/tags")
    public List<ResponseTag> getAllTags(
    ) {
        return tagServices.getTags();
    }

    @DeleteMapping(value = "/admin/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTag(
            @PathVariable UUID tagId
    ) {
        tagServices.deleteTag(tagId);
    }
}
