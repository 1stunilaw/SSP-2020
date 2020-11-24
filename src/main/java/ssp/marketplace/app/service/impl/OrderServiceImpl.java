package ssp.marketplace.app.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.ResponseOrderDto;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForOrder;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    private final DocumentService documentService;

    private final UserService userService;

    public OrderServiceImpl(
            OrderRepository orderRepository, UserRepository userRepository, TagRepository tagRepository,
            DocumentService documentService,
            UserService userService
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.documentService = documentService;
        this.userService = userService;
    }

    @Override
    public Page<ResponseOrderDto> getOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatusForOrderNotIn(pageable, Collections.singleton(StatusForOrder.DELETED));
        if (orders.isEmpty()) {
            throw new NotFoundException("Пусто");
        }
        Page<ResponseOrderDto> page =
                orders.map(ResponseOrderDto::responseOrderDtoFromOrder);
        return page;
    }

    @Override
    public ResponseOrderDto getOneOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        List<Document> activeDocuments = DocumentService.selectOnlyActiveDocument(order);
        order.setDocuments(activeDocuments);
        return ResponseOrderDto.responseOrderDtoFromOrder(order);
    }

    @Override
    public ResponseOrderDto addNewOrder(HttpServletRequest req, RequestOrderDto requestOrderDto) {
        Order order = saveUserAndOrder(req, requestOrderDto);
        Long number = orderRepository.getNumber(order.getName());
        order.setNumber(number);
        return ResponseOrderDto.responseOrderDtoFromOrder(order);
    }

    @Override
    public ResponseOrderDto addNewOrderWithDocuments(HttpServletRequest req, RequestOrderDto requestOrderDto, MultipartFile[] multipartFiles) {
        Order order = saveUserAndOrder(req, requestOrderDto);
        String pathS3 = "/" + order.getClass().getSimpleName() + "/" + order.getName();
        List<Document> documents = documentService.addNewDocuments(multipartFiles, pathS3, order);
        order.setDocuments(documents);
        orderRepository.save(order);
        Long number = orderRepository.getNumber(order.getName());
        order.setNumber(number);
        return ResponseOrderDto.responseOrderDtoFromOrder(order);
    }

    @Override
    public ResponseOrderDto editOrder(UUID id, RequestOrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (orderUpdateDto.getName() != null) {
            order.setName(orderUpdateDto.getName());
        }

        if (orderUpdateDto.getStatusForOrder() != null) {
            order.setStatusForOrder(orderUpdateDto.getStatusForOrder());
        }

        if (orderUpdateDto.getDescription() != null) {
            order.setDescription(orderUpdateDto.getDescription());
        }

        if (orderUpdateDto.getOrganizationName() != null) {
            order.setOrganizationName(orderUpdateDto.getOrganizationName());
        }

        if (orderUpdateDto.getDateStop()!= null) {
            LocalDate localDate = orderUpdateDto.getDateStop();
            LocalDateTime localDateTime = localDate.atStartOfDay().withHour(HOUR).withMinute(MINUTE);
            order.setDateStop(localDateTime);
        }

        if (orderUpdateDto.getTags()!= null) {
            List<String> tagsString = orderUpdateDto.getTags();
            setTagForOrder(order, tagsString);
        }
        orderRepository.save(order);
        return ResponseOrderDto.responseOrderDtoFromOrder(order);
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
            }
            catch (NotFoundException e){
                continue;
            }
        }
        orderRepository.save(order);
    }

    @Override
    public ResponseOrderDto markDoneOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        order.setStatusForOrder(StatusForOrder.CLOSED);
        orderRepository.save(order);
        return ResponseOrderDto.responseOrderDtoFromOrder(order);
    }

    private Order saveUserAndOrder(HttpServletRequest req, RequestOrderDto requestOrderDto) {
        if(orderRepository.findByName(requestOrderDto.getName()).isPresent()){
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
}
