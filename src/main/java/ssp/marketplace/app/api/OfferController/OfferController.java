package ssp.marketplace.app.api.OfferController;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;
import ssp.marketplace.app.service.OfferService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(value = "/{orderId}/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOfferDto createOffer(
            @PathVariable("orderId") UUID id,
            @ModelAttribute @Valid RequestOfferDto requestOfferDto,
            HttpServletRequest req
    ) {
        return offerService.createOffer(id, req, requestOfferDto);
    }

    @RequestMapping(value = "/{offerId}", method = RequestMethod.PATCH, consumes = {"multipart/form-data"})
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
    public void deleteDocumentFromOffer(
            @PathVariable UUID offerId,
            @PathVariable String name,
            HttpServletRequest req
    ) {
        offerService.deleteDocumentFromOffer(offerId, name, req);
    }

}
