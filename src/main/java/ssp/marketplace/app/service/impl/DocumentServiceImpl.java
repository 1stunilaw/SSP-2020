package ssp.marketplace.app.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.order.ResponseAddNewDocumentInOrder;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StateStatus;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final S3Services s3Services;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final OfferRepository offerRepository;

    public final DocumentRepository documentRepository;

    public final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    public DocumentServiceImpl(
            S3Services s3Services, MessageSource messageSource, UserRepository userRepository, OrderRepository orderRepository,
            UserService userService, OfferRepository offerRepository, DocumentRepository documentRepository,

            JwtTokenProvider jwtTokenProvider
    ) {
        this.s3Services = s3Services;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<Document> addNewDocuments(MultipartFile[] multipartFiles, String pathS3) {
        List<Document> documents = new ArrayList<>();
        //Проверка выбрал ли пользователь документ
        if (multipartFiles[0].getOriginalFilename().isEmpty()) {
            throw new BadRequestException("Документ не может быть пустым");
        }
        for (MultipartFile mf : multipartFiles
        ) {
            String fileName = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
            s3Services.uploadFile(mf, fileName, pathS3);
            Document document = new Document();
            document.setName(fileName);
            document.setStatusForDocument(StateStatus.ACTIVE);
            documents.add(document);
            documentRepository.save(document);
        }
        return documents;
    }

    @Override
    public ResponseAddNewDocumentInOrder addNewDocumentsInOrder(
            UUID id, MultipartFile[] multipartFiles
    ) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        int countActiveDocs = DocumentService.selectOnlyActiveDocument(order).size();
        int countNewDocs = multipartFiles.length;
        if (countActiveDocs + countNewDocs > 10) {
            String filesCountError = messageSource.getMessage("files.errors.amount", null, new Locale("ru", "RU"));
            throw new BadRequestException(filesCountError);
        }
        String pathS3 = "/" + order.getClass().getSimpleName() + "/" + order.getName();
        List<Document> documents = addNewDocuments(multipartFiles, pathS3);
        order.getDocuments().addAll(documents);
        orderRepository.save(order);
        List<String> strings = new ArrayList<>();
        for (Document doc : documents
        ) {
            strings.add(doc.getName());
        }
        return new ResponseAddNewDocumentInOrder(strings);
    }

    @Override
    public void deleteDocument(String name) {
        Document document = documentRepository.findByNameAndStatusForDocumentNotIn(
                name, Collections.singleton(StateStatus.DELETED))
                .orElseThrow(() -> new NotFoundException("Документ не найден"));
        document.setStatusForDocument(StateStatus.DELETED);
        documentRepository.save(document);
    }

    @Override
    public S3ObjectInputStream downloadSupplierFile(String keyName, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        String className = user.getClass().getSimpleName().split("\\$")[0];
        String path = "/" + className + "/" + user.getId();
        return s3Services.downloadFile(keyName, path);
    }

    @Override
    public S3ObjectInputStream downloadOrderFile(String keyName, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        String className = order.getClass().getSimpleName().split("\\$")[0];
        String path = "/" + className + "/" + order.getName();
        return s3Services.downloadFile(keyName, path);
    }

    @Override
    public S3ObjectInputStream downloadOfferFile(String keyName, UUID offerId, HttpServletRequest req) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Предложение не найдено"));

        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (!offer.getUser().getId().equals(userService.getUserFromHttpServletRequest(req).getId()) && !role.contains(RoleName.ROLE_ADMIN.toString())) {
            throw new AccessDeniedException("Доступ закрыт");
        }

        String className = offer.getClass().getSimpleName().split("\\$")[0];
        String path = "/" + className + "/" + offer.getNumber();
        return s3Services.downloadFile(keyName, path);
    }
}
