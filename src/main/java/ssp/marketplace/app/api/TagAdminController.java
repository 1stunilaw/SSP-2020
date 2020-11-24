package ssp.marketplace.app.api;

import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.service.TagServices;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/tags")
public class TagAdminController {

    private final TagServices tagServices;

    public TagAdminController(TagServices tagServices) {
        this.tagServices = tagServices;
    }

    @PostMapping(value = "/add-tag")
    public void addNewTag(
            @RequestBody @Valid @NotNull RequestTag requestTag
    ) {
        tagServices.addNewTag(requestTag);
    }
}
