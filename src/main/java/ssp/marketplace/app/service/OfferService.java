package ssp.marketplace.app.service;

import org.springframework.data.domain.*;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface OfferService {

    ResponseOfferDto createOffer(UUID id, HttpServletRequest req, RequestOfferDto requestOfferDto);

    ResponseOfferDto updateOffer(UUID id, RequestOfferDtoUpdate updateOfferDto);

    void deleteOffer(UUID id);

    ResponseOfferDtoAbstract getOneOffer(UUID id, HttpServletRequest req);

    Page<ResponseListOfferDto> getListOfOffers(Pageable pageable, UUID orderId, HttpServletRequest req);

}
