package ssp.marketplace.app.api.OfferController;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;
import ssp.marketplace.app.exceptions.BadRequestException;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/offers")
public class OfferController {

    private final OfferService offerService;

    private final DocumentService documentService;

    public OfferController(OfferService offerService, DocumentService documentService) {
        this.offerService = offerService;
        this.documentService = documentService;
    }


    @PostMapping(value = "/{orderId}/create", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOfferDto createOffer(
            @PathVariable("orderId") UUID id,
            @ModelAttribute @Valid RequestOfferDto requestOfferDto,
            HttpServletRequest req
    ) {
        return offerService.createOffer(id, req, requestOfferDto);
    }

    @PatchMapping (value = "/{offerId}", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseOfferDto updateOffer(
            @PathVariable("offerId") UUID id,
            @ModelAttribute @Valid RequestOfferDtoUpdate requestOfferDtoUpdate,
            HttpServletRequest req
    ) {
        return offerService.updateOffer(id, requestOfferDtoUpdate, req);
    }

    @DeleteMapping(value = "/{offerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOffer(
            @PathVariable("offerId") UUID id,
            HttpServletRequest req
    ) {
        // TODO: 20.12.2020 Добавить ответ
        offerService.deleteOffer(id, req);

    }

    @GetMapping("/{offerId}/show")
    public ResponseOfferDtoShow getOneOffer(
            @PathVariable("offerId") UUID id,
            HttpServletRequest req
    ) {
        return offerService.getOneOffer(id, req);
    }

    @GetMapping("/{orderId}")
    public Page<ResponseListOfferDto> getListOfOffers(
            @PageableDefault(sort = {"createdAt"},
                    size = 30, value = 30, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable("orderId") UUID id,
            HttpServletRequest req
    ) {
        return offerService.getListOfOffers(pageable, id, req);
    }

    @DeleteMapping("/{offerId}/document/{name}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteDocumentFromOffer(
            @PathVariable UUID offerId,
            @PathVariable String name,
            HttpServletRequest req
    ) {
        try {
            offerService.deleteDocumentFromOffer(offerId, name, req);
            return new SimpleResponse(HttpStatus.OK.value(), "Документ успешно удалён");
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидный ID предложения");
        }
    }

    @GetMapping(value = "/{offerId}/{filename}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InputStreamResource> getOfferDocument(
            @PathVariable String filename,
            @PathVariable UUID offerId
    ) {
        S3ObjectInputStream s3is = documentService.downloadOfferFile(filename, offerId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE)).cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(new InputStreamResource(s3is));
    }
}
