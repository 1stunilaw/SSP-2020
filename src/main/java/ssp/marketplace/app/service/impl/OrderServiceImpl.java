package ssp.marketplace.app.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    private final DocumentRepository documentRepository;

    private final DocumentService documentService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    public OrderServiceImpl(
            OrderRepository orderRepository, UserRepository userRepository, TagRepository tagRepository,
            DocumentRepository documentRepository, DocumentService documentService,
            UserService userService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.documentRepository = documentRepository;
        this.documentService = documentService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Page<ResponseListOrderDto> getOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatusForOrderNotIn(pageable, Collections.singleton(StatusForOrder.DELETED));
        if (orders.isEmpty()) {
            throw new NotFoundException("Пусто");
        }
        Page<ResponseListOrderDto> page =
                orders.map(ResponseListOrderDto::responseOrderDtoFromOrder);
        return page;
    }

    @Override
    public ResponseOneOrderDtoAbstract getOneOrder(UUID id, HttpServletRequest req) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        List<Document> activeDocuments = DocumentService.selectOnlyActiveDocument(order);
        order.setDocuments(activeDocuments);
        String token = jwtTokenProvider.resolveToken(req);
        List<String> role = jwtTokenProvider.getRole(token);
        if (role.contains(RoleName.ROLE_ADMIN.toString())) {
            return ResponseOneOrderDtoAdmin.responseOrderDtoFromOrder(order);
        }
        return ResponseOneOrderDto.responseOrderDtoFromOrder(order);
    }

    @Override
    public ResponseOneOrderDtoAdmin createOrder(HttpServletRequest req, String dtoString, MultipartFile[] multipartFiles) {
        RequestOrderDto dtoObject = RequestOrderDto.convert(dtoString);
        Order order = saveUserAndOrder(req, dtoObject);
        Long number = orderRepository.getNumber(order.getName());
        order.setNumber(number);
        if (multipartFiles != null && multipartFiles.length != 0) {
            addDocumentToOrder(order, multipartFiles);
        }
        return ResponseOneOrderDtoAdmin.responseOrderDtoFromOrder(order);
    }

    @Override
    public ResponseOneOrderDtoAdmin editOrder(UUID id, String dtoString, MultipartFile[] multipartFiles) {
        RequestOrderUpdateDto dtoObject = RequestOrderUpdateDto.convert(dtoString);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));

        List<Document> documents = order.getDocuments();
        List<String> documentsUpdate = dtoObject.getDocuments();
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

        if (multipartFiles != null && multipartFiles.length != 0) {
            addDocumentToOrder(order, multipartFiles);
        }

        if (dtoObject.getName() != null) {
            order.setName(dtoObject.getName());
        }

        if (dtoObject.getStatusForOrder() != null) {
            order.setStatusForOrder(dtoObject.getStatusForOrder());
        }

        if (dtoObject.getDescription() != null) {
            order.setDescription(dtoObject.getDescription());
        }

        if (dtoObject.getOrganizationName() != null) {
            order.setOrganizationName(dtoObject.getOrganizationName());
        }

        if (dtoObject.getDateStop() != null) {
            LocalDate localDate = dtoObject.getDateStop();
            LocalDateTime localDateTime = localDate.atStartOfDay().withHour(HOUR).withMinute(MINUTE);
            order.setDateStop(localDateTime);
        }

        if (dtoObject.getTags() != null) {
            List<String> tagsString = dtoObject.getTags();
            setTagForOrder(order, tagsString);
        }
        orderRepository.save(order);
        return ResponseOneOrderDtoAdmin.responseOrderDtoFromOrder(order);
    }

    @Override
    public void deleteOrder(UUID id) {
        Order order = orderRepository.findByIdAndStatusForOrderNotIn(id, Collections.singleton(StatusForOrder.DELETED))
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        order.setStatusForOrder(StatusForOrder.DELETED);
        List<Document> documents = order.getDocuments();
        for (Document doc : documents
        ) {
            try {
                documentService.deleteDocument(doc.getName());
            } catch (NotFoundException e) {
                continue;
            }
        }
        orderRepository.save(order);
    }

    @Override
    public ResponseListOrderDto markDoneOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        order.setStatusForOrder(StatusForOrder.CLOSED);
        orderRepository.save(order);
        return ResponseListOrderDto.responseOrderDtoFromOrder(order);
    }

    private Order saveUserAndOrder(HttpServletRequest req, RequestOrderDto requestOrderDto) {
        if (orderRepository.findByName(requestOrderDto.getName()).isPresent()) {
            throw new AlreadyExistsException("Заказ с таким именнем уже существует");
        }
        Order order = OrderService.orderFromOrderDto(requestOrderDto);
        List<String> tags = requestOrderDto.getTags();
        setTagForOrder(order, tags);

        User userFromDB = userService.getUserFromHttpServletRequest(req);
        List<Order> ordersFromUser = userFromDB.getOrders();
        order.setUser(userFromDB);
        ordersFromUser.add(order);
        orderRepository.save(order);
        userRepository.save(userFromDB);
        return order;
    }

    private Order setTagForOrder(Order order, List<String> tags) {
        List<Tag> orderTags = new ArrayList<>();
        if (tags != null) {
            for (String tagName : tags
            ) {
                Tag tagFromDB = tagRepository.findByTagName(tagName);
                tagFromDB.getOrdersList().add(order);
                orderTags.add(tagFromDB);//
                tagRepository.save(tagFromDB);
            }
        }
        order.setTags(orderTags);
        return order;
    }

    private void addDocumentToOrder(
            Order order,
            MultipartFile[] multipartFiles
    ) {
        String pathS3 = "/" + order.getClass().getSimpleName() + "/" + order.getName();
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3);
        if (order.getDocuments() != null) {
            order.getDocuments().addAll(documents);
        } else {
            order.setDocuments(documents);
        }
        orderRepository.save(order);
    }
}
