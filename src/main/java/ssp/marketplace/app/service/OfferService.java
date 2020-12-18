package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface OfferService {

    ResponseOfferDtoAdmin createOffer(UUID id, HttpServletRequest req, RequestOfferDto requestOfferDto);

    ResponseOfferDtoAdmin updateOffer(UUID id, RequestOfferDtoUpdate updateOfferDto, HttpServletRequest req);

    void deleteOffer(UUID id, HttpServletRequest req);

    ResponseOfferDtoAbstract getOneOffer(UUID id, HttpServletRequest req);

    Page<ResponseListOfferDto> getListOfOffers(Pageable pageable, UUID orderId, HttpServletRequest req);

    void deleteDocumentFromOffer(UUID id, String name, HttpServletRequest req);

}
