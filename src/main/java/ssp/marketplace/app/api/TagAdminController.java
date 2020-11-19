package ssp.marketplace.app.api;

import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.requestDto.RequestTag;
import ssp.marketplace.app.service.TagServices;

@RestController
@RequestMapping("/admin/tags")
public class TagAdminController {

    private final TagServices tagServices;

    public TagAdminController(TagServices tagServices) {
        this.tagServices = tagServices;
    }

    @PostMapping(value = "/add-tag")
    public void addNewTag(
            @RequestBody RequestTag requestTag
    ) {
        tagServices.addNewTag(requestTag);
    }
}
