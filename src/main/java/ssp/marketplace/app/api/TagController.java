package ssp.marketplace.app.api;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.*;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteTag(
            @PathVariable UUID tagId
    ) {
        tagServices.deleteTag(tagId);
        // TODO: 20.12.2020 Переделать в дто
        HashMap response = new HashMap();
        response.put("status", HttpStatus.OK);
        response.put("message", "Тег успешно удалён");
        // TODO: 20.12.2020 Статус
        return new ResponseEntity(response, HttpStatus.OK);
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
