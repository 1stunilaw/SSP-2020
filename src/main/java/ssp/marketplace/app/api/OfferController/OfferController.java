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

/**
 * Контроллер для работы с предложениями
 */
@RestController
@RequestMapping("api/offers")
public class OfferController {

    private final OfferService offerService;

    private final DocumentService documentService;

    public OfferController(OfferService offerService, DocumentService documentService) {
        this.offerService = offerService;
        this.documentService = documentService;
    }

    /**
     * Запрос на отправку предложения для заказа
     * @param orderId id заказа
     * @param requestOfferDto входные данные для создания
     * @param req информация о запросе
     * @return информацию о созданном предложении
     */
    @PostMapping(value = "/{orderId}/create", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOfferDto createOffer(
            @PathVariable("orderId") UUID orderId,
            @ModelAttribute @Valid RequestOfferDto requestOfferDto,
            HttpServletRequest req
    ) {
        return offerService.createOffer(orderId, req, requestOfferDto);
    }

    /**
     * Запрос на изменение выбранного предложения
     * @param offerId id изменяемого предложения
     * @param requestOfferDtoUpdate входные данные для изменения предложения
     * @param req информация о запросе
     * @return информация об измененном предложении
     */
    @PatchMapping (value = "/{offerId}", consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseOfferDto updateOffer(
            @PathVariable("offerId") UUID offerId,
            @ModelAttribute @Valid RequestOfferDtoUpdate requestOfferDtoUpdate,
            HttpServletRequest req
    ) {
        return offerService.updateOffer(offerId, requestOfferDtoUpdate, req);
    }

    /**
     * Запрос на удаление выбранного предложения
     * @param offerId id удаляемого предложения
     * @param req информация о запросе
     * @return сообщение, информирующее о результате запроса
     */
    @DeleteMapping(value = "/{offerId}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteOffer(
            @PathVariable("offerId") UUID offerId,
            HttpServletRequest req
    ) {
            try {
                offerService.deleteOffer(offerId, req);
                return new SimpleResponse(HttpStatus.OK.value(), "Предложение успешно удалено");
            } catch (IllegalArgumentException ex){
                throw new BadRequestException("Невалидный ID предложения");
            }
    }

    /**
     * Запрос на получение подробной информации предложения
     * @param offerId id просамтриваемого предложения
     * @param req информация о запросе
     * @return информация о просматриваемом предложении
     */
    @GetMapping("/{offerId}/show")
    public ResponseOfferDtoShow getOneOffer(
            @PathVariable("offerId") UUID offerId,
            HttpServletRequest req
    ) {
        return offerService.getOneOffer(offerId, req);
    }

    /**
     * Запрос на получение всех предложений к выбранному заказу
     * @param pageable страница списка
     * @param orderId id просматриваемого заказа
     * @param req информация о запросе
     * @return список предложений к заказу
     */
    @GetMapping("/{orderId}")
    public Page<ResponseListOfferDto> getListOfOffers(
            @PageableDefault(sort = {"createdAt"},
                    size = 30, value = 30, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable("orderId") UUID orderId,
            HttpServletRequest req
    ) {
        return offerService.getListOfOffers(pageable, orderId, req);
    }

    /**
     * Запрос на удаление файла из предложения
     * @param filename имя удаляемого файла
     * @param offerId id предложения
     * @param req информация о запросе
     * @return сообщение, информирующее о результате
     */
    @DeleteMapping("/{offerId}/document/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse deleteDocumentFromOffer(
            @PathVariable String filename,
            @PathVariable UUID offerId,
            HttpServletRequest req
    ) {
        try {
            offerService.deleteDocumentFromOffer(filename, offerId, req);
            return new SimpleResponse(HttpStatus.OK.value(), "Документ успешно удалён");
        } catch (IllegalArgumentException ex){
            throw new BadRequestException("Невалидные ID предложения или имя файла");
        }
    }

    /**
     * Запрос на скачивание файла из предложения
     * @param filename имя удаляемого файла
     * @param offerId id предложения
     * @param req информация о запросе
     * @return сообщение, информирующее о результате
     */
    @GetMapping(value = "/{offerId}/{filename}", consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InputStreamResource> getOfferDocument(
            @PathVariable String filename,
            @PathVariable UUID offerId,
            HttpServletRequest req
    ) {
        try {
            return offerService.getOfferDocument(filename, offerId, req);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Невалидные ID предложения или имя файла");
        }
    }
}
