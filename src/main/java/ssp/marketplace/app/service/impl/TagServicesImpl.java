package ssp.marketplace.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.dto.requestDto.*;
import ssp.marketplace.app.dto.responseDto.ResponseTag;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.StatusForTag;
import ssp.marketplace.app.entity.supplier.SupplierDetails;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.repository.*;
import ssp.marketplace.app.service.TagServices;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServicesImpl implements TagServices {

    private final TagRepository tagRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public TagServicesImpl(TagRepository tagRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ResponseTag> getTags() {
        List<Tag> allTags = tagRepository.findAllByStatusForTagNotIn(Collections.singleton(StatusForTag.DELETED));
        List<ResponseTag> responseTagStream =
                allTags.stream().map(ResponseTag::getResponseTagFromTag).collect(Collectors.toList());
        return responseTagStream;
    }

    @Override
    public List<ResponseTag> addNewTag(RequestTag requestTag) {
        List<String> tagName = requestTag.getTagName().stream().map(String::toLowerCase).collect(Collectors.toList());
        List<ResponseTag> response = new ArrayList<>();
        for (String name : tagName) {
            Optional<Tag> byTagName = tagRepository.findByTagName(name);
            Tag tag;
            if (byTagName.isPresent()) {
                tag = byTagName.get();
                if(tag.getStatusForTag() == StatusForTag.DELETED){
                    tag.setStatusForTag(StatusForTag.ACTIVE);
                }
            }
            else {
                tag = new Tag();
                tag.setTagName(name);
                tag.setStatusForTag(StatusForTag.ACTIVE);
            }
            response.add(new ResponseTag(tagRepository.save(tag)));
        }

        return response;
    }

    @Override
    public void deleteTag(UUID id) {
        Tag tag = tagRepository.findByIdAndStatusForTagNotIn(id, Collections.singleton(StatusForTag.DELETED))
                .orElseThrow(() -> new NotFoundException("Тег не найден"));
        tag.setStatusForTag(StatusForTag.DELETED);
        delTagInOrders(tag);
        delTagInSupplierDetails(tag);
        tagRepository.save(tag);
    }

    @Override
    public ResponseTag editTag(UUID id, RequestUpdateTag requestUpdateTag) {
        Tag tag = tagRepository.findByIdAndStatusForTagNotIn(id, Collections.singleton(StatusForTag.DELETED))
                .orElseThrow(() -> new NotFoundException("Тег не найден"));
        String newTagName = requestUpdateTag.getTagName().toLowerCase();
        if (tagRepository.findByTagName(newTagName).isPresent()) {
            throw new BadRequestException("Тег с именем " + newTagName + " уже существует");
        }
        tag.setTagName(newTagName);
        return new ResponseTag(tagRepository.save(tag));
    }

    private void delTagInOrders(Tag tag) {
        List<Order> ordersList = tag.getOrdersList();
        if (ordersList != null) {
            for (Order o : ordersList
            ) {
                Set<Tag> tags = o.getTags();
                tags.remove(tag);
                orderRepository.save(o);
            }
        }
    }

    private void delTagInSupplierDetails(Tag tag) {
        List<SupplierDetails> suppliers = tag.getSuppliers();
        if (suppliers != null) {
            for (SupplierDetails supplier : suppliers
            ) {
                User user = supplier.getUser();
                Set<Tag> tags = supplier.getTags();
                tags.remove(tag);
                userRepository.save(user);
            }
        }
    }
}
