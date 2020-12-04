package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.user.supplier.LawStatusResponseDto;
import ssp.marketplace.app.service.LawStatusService;

import java.util.List;

@RestController
@RequestMapping("/api/law-statuses")
public class LawStatusController {

    private LawStatusService lawStatusService;

    @Autowired
    public LawStatusController(LawStatusService lawStatusService) {
        this.lawStatusService = lawStatusService;
    }

    @GetMapping()
    public List<LawStatusResponseDto> getAllStatuses(){
        return lawStatusService.getAllStatuses();
    }
}
