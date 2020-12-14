package ssp.marketplace.app.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.offer.requestDto.*;
import ssp.marketplace.app.dto.offer.responseDto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;
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

    private final DocumentService documentService;

    private final DocumentRepository documentRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public OfferServiceImpl(
            OfferRepository offerRepository, OrderRepository orderRepository, UserRepository userRepository,
            UserService userService, OrderService orderService,
            DocumentService documentService,
            DocumentRepository documentRepository,
            JwtTokenProvider jwtTokenProvider
    ) {

        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderService = orderService;
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
        orderFromDB.getOffers().add(offer);
        offer.setOrder(orderFromDB);

        User userFromDB = userService.getUserFromHttpServletRequest(req);
        userFromDB.getOffers().add(offer);
        offer.setUser(userFromDB);

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
    public ResponseOfferDto updateOffer(UUID id, RequestOfferDtoUpdate updateOfferDto){
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

        List<Document> documents = offer.getDocuments();
        List<String> documentsUpdate = updateOfferDto.getDocuments();
        if (documents != null && documentsUpdate != null) {
            List<String> documentsOld = new ArrayList<>();
            for (Document doc : documents
            ) {
                if (doc.getStatusForDocument() != StatusForDocument.DELETED) {
                    documentsOld.add(doc.getName());
                }
            }
            List<String> docDelete = new ArrayList<>(documentsOld);
            docDelete.removeAll(documentsUpdate);
            for (String docDelName : docDelete
            ) {
                if (documentRepository.findByName(docDelName) != null) {
                    documentService.deleteDocument(docDelName);
                } else {
                    throw new BadRequest("Файл " + docDelName + " не найден");
                }
            }
        }

        MultipartFile[] multipartFiles = updateOfferDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOffer(offer, multipartFiles);
        }

        if (updateOfferDto.getDescription() != null) {
            offer.setDescription(updateOfferDto.getDescription());
        }

        offerRepository.save(offer);
        return ResponseOfferDto.responseOfferDtoFromOffer(offer);
    }

    @Override
    public void deleteOffer(UUID id){
        Offer offer = offerRepository.findByIdAndStatusForOfferNotIn(id, Collections.singleton(StatusForOffer.DELETED))
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));
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
    public ResponseOfferDtoAbstract getOneOffer(UUID id, HttpServletRequest req){
        Offer offer = offerRepository.findByIdAndStatusForOfferNotIn(id, Collections.singleton(StatusForOffer.DELETED))
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));
        if (offer.getStatusForOffer() == StatusForOffer.DELETED) {
            throw new NotFoundException("Предложение удалено");
        }
        List<Document> activeDocuments = DocumentService.selectOnlyActiveOfferDocument(offer);
        offer.setDocuments(activeDocuments);

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (role.contains(RoleName.ROLE_ADMIN.toString())) {
            return ResponseOfferDtoAdmin.responseOfferDtoFromOffer(offer);
        }
        return ResponseOfferDto.responseOfferDtoFromOffer(offer);

    }
/*
    @Override
    public Page<ResponseListOfferDto> getListOfOffers(Pageable pageable){
        Page<Offer> offers = offerRepository.findByStatusForOfferNotIn(pageable, Collections.singleton(StatusForOffer.DELETED));
        if (offers.isEmpty()) {
            throw new NotFoundException("Пусто");
        }
        Page<ResponseListOfferDto> page =
                offers.map(ResponseListOfferDto::responseOfferDtoFromOffer);
        return page;
    }
*/
    private void addDocumentToOffer(
            Offer offer,
            MultipartFile[] multipartFiles
    ) {
        String pathS3 = "/" + offer.getClass().getSimpleName() + "/" + offer.getNumber(); // "/Offer/{уникальный номер}"
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3);
        if (offer.getDocuments() != null) {
            offer.getDocuments().addAll(documents);
        } else {
            offer.setDocuments(documents);
        }
        offerRepository.save(offer);
    }
}
