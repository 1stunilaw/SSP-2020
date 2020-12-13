package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;
import ssp.marketplace.app.service.impl.search.OrderSpecification;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final DocumentService documentService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final OrderBuilderService orderBuilderService;

    private final JwtTokenProvider jwtTokenProvider;

    public OrderServiceImpl(
            OrderRepository orderRepository, UserRepository userRepository,
            DocumentService documentService,
            MessageSource messageSource, UserService userService,
            OrderBuilderService orderBuilderService, JwtTokenProvider jwtTokenProvider
    ) {
        this.orderRepository = orderRepository;
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.documentService = documentService;
        this.userService = userService;
        this.orderBuilderService = orderBuilderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Page<ResponseListOrderDto> getOrders(Pageable pageable, String search) {
        Page<Order> orders;
        if (search != null && !StringUtils.isBlank(search)) {
            orders = orderRepository.findAll(OrderSpecification.search(search), pageable);
            List<Order> content = orders.getContent();
            Set<Order> hSet = new LinkedHashSet<>(content);
            List<Order> targetList = new ArrayList<>(hSet);
            orders = new PageImpl<>(targetList, pageable, (long)targetList.size());
        } else {
            orders = orderRepository.findByStatusForOrderNotIn(pageable, Collections.singleton(StatusForOrder.DELETED));
        }
        if (orders.isEmpty()) {
            throw new NotFoundException("Пусто");
        }
        Page<ResponseListOrderDto> page = orders.map(ResponseListOrderDto::responseOrderDtoFromOrder);
        return page;
    }



    @Override
    public ResponseOneOrderDtoAbstract getOneOrder(UUID id, HttpServletRequest req) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (order.getStatusForOrder() == StatusForOrder.DELETED) {
            throw new NotFoundException("Заказ удален");
        }
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
    public void deleteDocumentFromOrder(UUID id, String name) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        List<Document> documents = DocumentService.selectOnlyActiveDocument(order);
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

    @Override
    public ResponseOneOrderDtoAdmin createOrder(HttpServletRequest req, RequestOrderDto requestOrderDto) {
        if (orderRepository.findByName(requestOrderDto.getName()).isPresent()) {
            throw new AlreadyExistsException("Заказ с таким именнем уже существует");
        }
        LocalDate now = LocalDate.now();
        LocalDate dateStop = requestOrderDto.getDateStop();
        if (dateStop.isBefore(now)) {
            String dateError = messageSource.getMessage("dateStop.errors.before", null, new Locale("ru", "RU"));
            throw new BadRequestException(dateError);
        }

        Order order = orderBuilderService.orderFromOrderDto(requestOrderDto);
        User userFromDB = userService.getUserFromHttpServletRequest(req);
        userFromDB.getOrders().add(order);
        order.setUser(userFromDB);
        MultipartFile[] multipartFiles = requestOrderDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOrder(order, multipartFiles);
        }
        orderRepository.save(order);
        userRepository.save(userFromDB);
        order.setNumber(orderRepository.getNumber(order.getName()));/// TODO: 28.11.2020  
        return ResponseOneOrderDtoAdmin.responseOrderDtoFromOrder(order);
    }

    @Override
    public ResponseOneOrderDtoAdmin editOrder(UUID id, RequestOrderUpdateDto updateDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));

        String updateName = updateDto.getName();
        Optional<Order> byName = orderRepository.findByName(updateName);
        //если в бд существует заказ с таким же именем
        if (byName.isPresent() && !byName.get().getId().equals(id)) {
            throw new AlreadyExistsException("Заказ с таким именнем уже существует");
        }
        LocalDate now = LocalDate.now();
        LocalDate dateStop = updateDto.getDateStop();
        if (dateStop != null && dateStop.isBefore(now)) {
            String dateError = messageSource.getMessage("dateStop.errors.before", null, new Locale("ru", "RU"));
            throw new BadRequestException(dateError);
        }

        MultipartFile[] multipartFiles = updateDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOrder(order, multipartFiles);
        }

        if (updateName != null && !StringUtils.isBlank(updateName)) {
            order.setName(updateName);
        }
        StatusForOrder statusForOrder = updateDto.getStatusForOrder();
        if (statusForOrder != null) {
            order.setStatusForOrder(statusForOrder);
        }

        String description = updateDto.getDescription();
        if (description != null && !StringUtils.isBlank(description)) {
            order.setDescription(description);
        }

        String organizationName = updateDto.getOrganizationName();
        if (organizationName != null && !StringUtils.isBlank(organizationName)) {
            order.setOrganizationName(organizationName);
        }

        if (updateDto.getDateStop() != null) {
            LocalDate localDate = updateDto.getDateStop();
            LocalDateTime localDateTime = localDate.atStartOfDay().withHour(HOUR).withMinute(MINUTE);
            if (statusForOrder == StatusForOrder.CLOSED) {
                localDateTime = LocalDateTime.now();
                order.setDateStop(localDateTime);
            }
            order.setDateStop(localDateTime);
        }

        if (updateDto.getTags() != null) {
            List<UUID> tagsId = updateDto.getTags();
            orderBuilderService.setTagForOrder(order, tagsId);
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
    public void deleteOrderTags(UUID id, RequestDeleteTags requestDeleteTags) {
        Order order = orderRepository.findByIdAndStatusForOrderNotIn(id, Collections.singleton(StatusForOrder.DELETED))
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        Set<UUID> tagsId = requestDeleteTags.getTagsId();
        Set<Tag> tags = order.getTags();
        tags.removeIf(t -> tagsId.contains(t.getId()));
        order.setTags(tags);
        orderRepository.save(order);
    }

    @Override
    public ResponseOneOrderDtoAdmin markDoneOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        order.setStatusForOrder(StatusForOrder.CLOSED);
        order.setDateStop(LocalDateTime.now());
        orderRepository.save(order);
        return ResponseOneOrderDtoAdmin.responseOrderDtoFromOrder(order);
    }

    private void addDocumentToOrder(
            Order order,
            MultipartFile[] multipartFiles
    ) {
        List<Document> oldDocuments = DocumentService.selectOnlyActiveDocument(order);
        int countOldDoc = oldDocuments.size();
        int countNewDoc = multipartFiles.length;

        if (countOldDoc + countNewDoc > 10) {
            String filesCountError = messageSource.getMessage("files.errors.amount", null, new Locale("ru", "RU"));
            throw new BadRequestException(filesCountError);
        }
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
