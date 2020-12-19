package ssp.marketplace.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
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

    private final UserRepository userRepository;

    private final UserService userService;

    private final OrderService orderService;

    private final MessageSource messageSource;

    private final DocumentService documentService;

    private final DocumentRepository documentRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public OfferServiceImpl(
            OfferRepository offerRepository, OrderRepository orderRepository, UserRepository userRepository,
            UserService userService, OrderService orderService,
            MessageSource messageSource, DocumentService documentService,
            DocumentRepository documentRepository,
            JwtTokenProvider jwtTokenProvider
    ) {

        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.messageSource = messageSource;
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseOfferDto createOffer(UUID id, HttpServletRequest req, RequestOfferDto requestOfferDto) {

        /**
         * id предложения /
         * id заказа +
         * id поставщика (организация) +
         * порядковый номер предложения /
         * статус +
         * описание +
         * дата создания /
         * дата изменения /
         * документы +
         */

        Offer offer = new Offer();

        Order orderFromDB = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (orderFromDB.getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (role.contains(RoleName.ROLE_BLANK_USER.toString())) {
            String filesCountError = messageSource.getMessage("offers.errors.empty", null, new Locale("ru", "RU"));
            throw new BadRequestException(filesCountError);
        }
        if (role.contains(RoleName.ROLE_ADMIN.toString())) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        User userFromDB = userService.getUserFromHttpServletRequest(req);
        userFromDB.getOffers().add(offer);
        offer.setUser(userFromDB);

        orderFromDB.getOffers().add(offer);
        offer.setOrder(orderFromDB);

        offer.setStatusForOffer(StatusForOffer.ACTIVE);
        offer.setDescription(requestOfferDto.getDescription());

        MultipartFile[] multipartFiles = requestOfferDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOffer(offer, multipartFiles);
        }
        offerRepository.save(offer);
        userRepository.save(userFromDB);
        orderRepository.save(orderFromDB);

        return ResponseOfferDto.responseOfferDtoFromOffer(offer);
    }

    @Override
    public ResponseOfferDto updateOffer(UUID id, RequestOfferDtoUpdate updateOfferDto, HttpServletRequest req) {
        /**
         * описание +
         * дата изменения TODO: проверка наличия изменений чуть позже
         * документы +
         */

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));
        if (offer.getStatusForOffer() == StatusForOffer.DELETED) {
            throw new NotFoundException("Предложение удалено");
        }

        if (offer.getOrder().getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }

        if ((offer.getUser().getId() != userService.getUserFromHttpServletRequest(req).getId())) {
            throw new AccessDeniedException("Доступ закрыт");
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
    public void deleteOffer(UUID id, HttpServletRequest req) {
        Offer offer = offerRepository.findByIdAndStatusForOfferNotIn(id, Collections.singleton(StatusForOffer.DELETED))
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if ((offer.getUser().getId() != userService.getUserFromHttpServletRequest(req).getId()) && !(role.contains(RoleName.ROLE_ADMIN.toString()))) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        offer.setStatusForOffer(StatusForOffer.DELETED);
        List<Document> documents = offer.getDocuments();
        for (Document doc : documents
        ) {
            try {
                documentService.deleteDocument(doc.getName());
            } catch (NotFoundException e) {
                continue;
            }
        }
        offerRepository.save(offer);
    }

    @Override
    public ResponseOfferDtoShow getOneOffer(UUID id, HttpServletRequest req) {
        Offer offer = offerRepository.findByIdAndStatusForOfferNotIn(id, Collections.singleton(StatusForOffer.DELETED))
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));
        if (offer.getStatusForOffer() == StatusForOffer.DELETED) {
            throw new NotFoundException("Предложение удалено");
        }
        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if ((offer.getUser().getId() != userService.getUserFromHttpServletRequest(req).getId()) && !(role.contains(RoleName.ROLE_ADMIN.toString()))) {
            throw new AccessDeniedException("Доступ закрыт");
        }
        List<Document> activeDocuments = DocumentService.selectOnlyActiveOfferDocument(offer);
        offer.setDocuments(activeDocuments);
        return ResponseOfferDtoShow.responseOfferDtoFromOffer(offer);
    }

    @Override
    public Page<ResponseListOfferDto> getListOfOffers(Pageable pageable, UUID orderId, HttpServletRequest req) {

        Page<Offer> offers;
        Page<ResponseListOfferDto> page = null;

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (role.contains(RoleName.ROLE_ADMIN.toString())) {
            offers = offerRepository.findByOrderIdAndStatusForOffer(pageable, orderId, StatusForOffer.ACTIVE);
            page = offers.map(ResponseListOfferDto::responseOfferDtoFromOffer);
            return page;
        }
        User user = userService.getUserFromHttpServletRequest(req);
        offers = offerRepository.findByOrderIdAndUserIdAndStatusForOffer(pageable, orderId, user.getId(), StatusForOffer.ACTIVE);
        page = offers.map(ResponseListOfferDto::responseOfferDtoFromOffer);

        return page;
    }

    private void addDocumentToOffer(Offer offer, MultipartFile[] multipartFiles) {
        List<Document> oldDocuments = DocumentService.selectOnlyActiveOfferDocument(offer);
     if ((oldDocuments.size() + multipartFiles.length) > 10) {
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
    public void deleteDocumentFromOffer(UUID id, String name, HttpServletRequest req) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if ((offer.getUser().getId() != userService.getUserFromHttpServletRequest(req).getId())) {
            throw new AccessDeniedException("Доступ закрыт");
        }
        List<Document> documents = DocumentService.selectOnlyActiveOfferDocument(offer);
        List<String> names = new ArrayList<>();
        for (Document doc : documents
        ) {
            names.add(doc.getName());
        }
        if (names.contains(name)) {
            documentService.deleteDocument(name);
        } else {
            throw new NotFoundException("Документ не найден");
        }
    }
}
