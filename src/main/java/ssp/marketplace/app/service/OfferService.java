package ssp.marketplace.app.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface OfferService {

    ResponseOfferDto createOffer(UUID id, HttpServletRequest req, RequestOfferDto requestOfferDto);

    ResponseOfferDto updateOffer(UUID id, RequestOfferDtoUpdate updateOfferDto, HttpServletRequest req);

    void deleteOffer(UUID id, HttpServletRequest req);

    ResponseOfferDtoShow getOneOffer(UUID id, HttpServletRequest req);

    Page<ResponseListOfferDto> getListOfOffers(Pageable pageable, UUID orderId, HttpServletRequest req);

    ResponseEntity<InputStreamResource> getOfferDocument(String filename, UUID offerId, HttpServletRequest req);

    void deleteDocumentFromOffer(String filename, UUID offerId, HttpServletRequest req);

}
