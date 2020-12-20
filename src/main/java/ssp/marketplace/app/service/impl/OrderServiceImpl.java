package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.*;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.security.jwt.JwtTokenProvider;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final int HOUR = 23;

    private static final int MINUTE = 59;

    @Value("${frontend.url}")
    private String frontendUrl;

    private final OrderRepository orderRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final MailService mailService;

    private final UserRepository userRepository;

    private final DocumentService documentService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final OrderBuilderService orderBuilderService;

    private final JwtTokenProvider jwtTokenProvider;

    private final RoleRepository roleRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository, ApplicationEventPublisher eventPublisher, MailService mailService,
            UserRepository userRepository,
            DocumentService documentService,
            MessageSource messageSource, UserService userService,
            OrderBuilderService orderBuilderService, JwtTokenProvider jwtTokenProvider,
            RoleRepository roleRepository
    ) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.userRepository = userRepository;
        this.documentService = documentService;
        this.userService = userService;
        this.orderBuilderService = orderBuilderService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<ResponseListOrderDto> getOrders(Pageable pageable, String textSearch, String status) {
        Page<ResponseListOrderDto> page = null;
        if (StringUtils.isBlank(textSearch) && StringUtils.isBlank(status)) {
            Page<Order> orders = orderRepository.findByStatusForOrderNotIn(pageable, Collections.singleton(StatusForOrder.DELETED));
            page = orders.map(ResponseListOrderDto::responseOrderDtoFromOrder);
            return page;
        }
        if (!StringUtils.isBlank(textSearch) && !StringUtils.isBlank(status)) {
            Set<Order> search = orderRepository.searchAndFilterStatus(textSearch, status);
            page = mapToDtoAndToPages(search, pageable);
            return page;
        }
        if (!StringUtils.isBlank(status)) {
            Set<Order> search = orderRepository.filterStatus(status);
            page = mapToDtoAndToPages(search, pageable);
            return page;
        }

        if (!StringUtils.isBlank(textSearch)) {
            Set<Order> search = orderRepository.search(textSearch);
            page = mapToDtoAndToPages(search, pageable);
            return page;
        }
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
        // TODO: 20.12.2020 Переделать удаление через id 
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
        // TODO: 20.12.2020 Переделать проверку через @Unique в дто 
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

        // TODO: 20.12.2020 Попробовать сделать без добавления заказ к пользователю. Если будет работать, то убрать его сохранение 
        User userFromDB = userService.getUserFromHttpServletRequest(req);
        userFromDB.getOrders().add(order);
        order.setUser(userFromDB);
        MultipartFile[] multipartFiles = requestOrderDto.getFiles();
        if (multipartFiles != null) {
            addDocumentToOrder(order, multipartFiles);
        }
        orderRepository.save(order);
        userRepository.save(userFromDB);
        sendConfirmationEmail(order);
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
            order.setDateStop(localDateTime);
        }

        if (updateDto.getTags() != null) {
            List<UUID> tagsId = updateDto.getTags();
            orderBuilderService.setTagForOrder(order, tagsId);
        }

        StatusForOrder statusForOrder = updateDto.getStatusForOrder();
        if (statusForOrder != null) {
            if (statusForOrder == StatusForOrder.CLOSED) {
                order.setDateStop(LocalDateTime.now());
            }
            order.setStatusForOrder(statusForOrder);
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
            } catch (NotFoundException ignored) {
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

        // TODO: 20.12.2020 Убрать одну из проверок - либо тут, либо в DocumentService
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

    private Page<ResponseListOrderDto> mapToDtoAndToPages(Set<Order> search, Pageable pageable) {
        List<Order> targetList = new ArrayList<>(search);
        int start = (int)pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), targetList.size());
        if (end < start) {
            Page<Order> orders = new PageImpl<>(targetList.subList(0, 0), pageable, 0);
            Page<ResponseListOrderDto> page = orders.map(ResponseListOrderDto::responseOrderDtoFromOrder);
            return page;
        }
        Page<Order> orders = new PageImpl<>(targetList.subList(start, end), pageable, (long)targetList.size());
        Page<ResponseListOrderDto> page = orders.map(ResponseListOrderDto::responseOrderDtoFromOrder);
        return page;
    }

    // TODO: 20.12.2020 Переименовать метод
    @Async
    public void sendConfirmationEmail(Order order) {
        try {
            List<Role> roles = roleRepository.findByNameIsIn(Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_BLANK_USER));
            List<User> userList = userRepository.findByRolesInAndStatus(roles, UserStatus.ACTIVE);
            Map<String, Object> data = new HashMap<>();
            String orderId = order.getId().toString();
            data.put("url", frontendUrl + "/orders/" + orderId);
            data.put("theme", order.getName());
            mailService.sendMassMail("new_order", "Доступен новый заказ", data, userList);
        } catch (Exception ignored) {
        }
    }
}
