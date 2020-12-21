package ssp.marketplace.app.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.dto.tag.*;
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
    public List<ResponseTag> addNewTag(
            @RequestBody @Valid RequestTag requestTag
    ) {
        return tagServices.addNewTag(requestTag);
    }

    @GetMapping("/tags")
    public List<ResponseTag> getAllTags(
    ) {
        return tagServices.getTags();
    }

    @DeleteMapping(value = "/admin/tags/{tagId}")
    public SimpleResponse deleteTag(
            @PathVariable UUID tagId
    ) {
        tagServices.deleteTag(tagId);
        return new SimpleResponse(HttpStatus.OK.value(), "Тег успешно удалён");
    }

    @PatchMapping(value = "/admin/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseTag updateTag(
            @PathVariable UUID tagId,
            @RequestBody @Valid RequestUpdateTag requestUpdateTag
    ) {
        return tagServices.editTag(tagId, requestUpdateTag);
    }
}
