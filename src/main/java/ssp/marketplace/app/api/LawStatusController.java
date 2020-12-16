package ssp.marketplace.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.lawStatus.LawStatusCreateRequestDto;
import ssp.marketplace.app.dto.user.supplier.LawStatusResponseDto;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.service.LawStatusService;

import javax.validation.Valid;
import java.util.*;

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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public LawStatusResponseDto createLawStatus(@RequestBody @Valid LawStatusCreateRequestDto dto){
        return lawStatusService.createLawStatus(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLawStatus(@PathVariable("id") String id){
        try {
            lawStatusService.deleteLawStatus(UUID.fromString(id));
            HashMap response = new HashMap();
            response.put("status", HttpStatus.OK);
            response.put("message", "Юридический статус успешно удалён");
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный ID юридического статуса");
        }
    }
}
