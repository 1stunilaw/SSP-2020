package ssp.marketplace.app.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final OrderRepository orderRepository;

    private final UserService userService;

    private final MessageSource messageSource;

    private final DocumentService documentService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MailService mailService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public OfferServiceImpl(
            OfferRepository offerRepository, OrderRepository orderRepository, UserService userService,
            MessageSource messageSource, DocumentService documentService,
            JwtTokenProvider jwtTokenProvider,
            MailService mailService
    ) {
        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.messageSource = messageSource;
        this.documentService = documentService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailService = mailService;
    }

    @Override
    public ResponseOfferDto createOffer(UUID orderId, HttpServletRequest req, RequestOfferDto requestOfferDto) {

        Offer offer = new Offer();

        Order orderFromDB = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (orderFromDB.getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }
        if (!(orderFromDB.getStatusForOrder() == StatusForOrder.WAITING_OFFERS)) {
            String notWaitingOffers = messageSource.getMessage("offers.errors.late", null, new Locale("ru", "RU"));
            throw new BadRequestException(notWaitingOffers);
        }
        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (role.contains(RoleName.ROLE_BLANK_USER.toString())) {
            String notFullInformation = messageSource.getMessage("offers.errors.contacts", null, new Locale("ru", "RU"));
            throw new BadRequestException(notFullInformation);
        }
        if (role.contains(RoleName.ROLE_ADMIN.toString())) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        //если номер предложения формируется внутри каждого заказа
        //List <Offer> offers = offerRepository.findByOrderId(id);

        List<Offer> offers = offerRepository.findAll();
        Long number = (long)(offers.size() + 1);
        offer.setNumber(number);

        User userFromDB = userService.getUserFromHttpServletRequest(req);
        offer.setUser(userFromDB);

        offer.setOrder(orderFromDB);

        offer.setStateStatus(StateStatus.ACTIVE);
        offer.setDescription(requestOfferDto.getDescription());

        MultipartFile[] multipartFiles = requestOfferDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOffer(offer, multipartFiles);
        }
        Offer savedOffer = offerRepository.save(offer);
        sendOfferNotification(savedOffer);
        return ResponseOfferDto.responseOfferDtoFromOffer(offer);
    }

    @Async
    void sendOfferNotification(Offer offer) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("companyName", offer.getUser().getSupplierDetails().getCompanyName());
        data.put("orderUrl", frontendUrl + "/orders/" + offer.getOrder().getId());
        data.put("offerUrl", frontendUrl + "/offers/" + offer.getId() + "/show");

        mailService.sendMail("new_offer", "Новое предложение к вашему заказу", data, offer.getOrder().getUser());
    }

    @Override
    public ResponseOfferDto updateOffer(UUID offerId, RequestOfferDtoUpdate updateOfferDto, HttpServletRequest req) {

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));
        if (offer.getStateStatus() == StateStatus.DELETED) {
            throw new NotFoundException("Предложение удалено");
        }

        if (offer.getOrder().getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }

        if (!offer.getUser().getId().equals(userService.getUserFromHttpServletRequest(req).getId())) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        if (!(offer.getOrder().getStatusForOrder() == StatusForOrder.WAITING_OFFERS)) {
            String notWaitingOffers = messageSource.getMessage("offers.errors.late", null, new Locale("ru", "RU"));
            throw new BadRequestException(notWaitingOffers);
        }

        MultipartFile[] multipartFiles = updateOfferDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOffer(offer, multipartFiles);
        }

        String description = updateOfferDto.getDescription();
        if (updateOfferDto.getDescription() != null && !StringUtils.isBlank(description)) {
            offer.setDescription(updateOfferDto.getDescription());
        }

        offerRepository.save(offer);
        return ResponseOfferDto.responseOfferDtoFromOffer(offer);
    }

    @Override
    public void deleteOffer(UUID offerId, HttpServletRequest req) {
        Offer offer = offerRepository.findByIdAndStateStatus(offerId, StateStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));

        if (offer.getOrder().getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (!offer.getUser().getId().equals(userService.getUserFromHttpServletRequest(req).getId()) && !role.contains(RoleName.ROLE_ADMIN.toString())) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        offer.setStateStatus(StateStatus.DELETED);
        List<Document> documents = offer.getDocuments();
        for (Document doc : documents
        ) {
            try {
                documentService.deleteDocument(doc.getName());
            } catch (NotFoundException ignored) {

            }
        }
        offerRepository.save(offer);
    }

    @Override
    public ResponseOfferDtoShow getOneOffer(UUID offerId, HttpServletRequest req) {
        Offer offer = offerRepository.findByIdAndStateStatus(offerId, StateStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));
        if (offer.getStateStatus() == StateStatus.DELETED) {
            throw new NotFoundException("Предложение удалено");
        }
        if (offer.getOrder().getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }
        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (!offer.getUser().getId().equals(userService.getUserFromHttpServletRequest(req).getId()) && !role.contains(RoleName.ROLE_ADMIN.toString())) {
            throw new AccessDeniedException("Доступ закрыт");
        }
        List<Document> activeDocuments = DocumentService.selectOnlyActiveOfferDocument(offer);
        offer.setDocuments(activeDocuments);
        return ResponseOfferDtoShow.responseOfferDtoFromOffer(offer);
    }

    @Override
    public Page<ResponseListOfferDto> getListOfOffers(Pageable pageable, UUID orderId, HttpServletRequest req) {

        Order orderFromDB = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (orderFromDB.getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }
        Page<Offer> offers;
        Page<ResponseListOfferDto> page;

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (role.contains(RoleName.ROLE_ADMIN.toString())) {
            offers = offerRepository.findByOrderIdAndStateStatus(pageable, orderId, StateStatus.ACTIVE);
            page = offers.map(ResponseListOfferDto::responseOfferDtoFromOffer);
            return page;
        }
        User user = userService.getUserFromHttpServletRequest(req);
        offers = offerRepository.findByOrderIdAndUserIdAndStateStatus(pageable, orderId, user.getId(), StateStatus.ACTIVE);
        page = offers.map(ResponseListOfferDto::responseOfferDtoFromOffer);

        return page;
    }

    private void addDocumentToOffer(Offer offer, MultipartFile[] multipartFiles) {
        List<Document> oldDocuments = DocumentService.selectOnlyActiveOfferDocument(offer);
        if (oldDocuments.size() + multipartFiles.length > 10) {
            String filesCountError = messageSource.getMessage("files.errors.amount", null, new Locale("ru", "RU"));
            throw new BadRequestException(filesCountError);
        }
        String pathS3 = "/" + offer.getClass().getSimpleName() + "/" + offer.getNumber(); // "/Offer/{уникальный номер}"
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3);
        if (offer.getDocuments() != null) {
            offer.getDocuments().addAll(documents);
        } else {
            offer.setDocuments(documents);
        }
        offerRepository.save(offer);
    }

    @Override
    public ResponseEntity<InputStreamResource> getOfferDocument(
            String filename, UUID offerId, HttpServletRequest req
    ) {
        Offer offer = offerRepository.findByIdAndStateStatus(offerId, StateStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));

        if (offer.getOrder().getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }
        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (!offer.getUser().getId().equals(userService.getUserFromHttpServletRequest(req).getId()) && !role.contains(RoleName.ROLE_ADMIN.toString())) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        S3ObjectInputStream s3is = documentService.downloadOfferFile(filename, offerId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE)).cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(new InputStreamResource(s3is));
    }

    @Override
    public void deleteDocumentFromOffer(String filename, UUID offerId, HttpServletRequest req) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (!offer.getUser().getId().equals(userService.getUserFromHttpServletRequest(req).getId())) {
            throw new AccessDeniedException("Доступ закрыт");
        }
        List<Document> documents = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> names = new ArrayList<>();
        for (Document doc : documents
        ) {
            names.add(doc.getName());
        }
        if (names.contains(filename)) {
            documentService.deleteDocument(filename);
        } else {
            throw new NotFoundException("Документ не найден");
        }
    }
}
