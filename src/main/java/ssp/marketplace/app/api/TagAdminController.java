package ssp.marketplace.app.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.service.TagServices;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/tags")
public class TagAdminController {

    private final TagServices tagServices;

    public TagAdminController(TagServices tagServices) {
        this.tagServices = tagServices;
    }

    @PostMapping(value = "/add-tag")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewTag(
            @RequestBody @Valid RequestTag requestTag
    ) {
        tagServices.addNewTag(requestTag);
    }

    @GetMapping()
    public List<String> getAllTags(
    ) {
        return tagServices.getTags();
    }
}
