package ssp.marketplace.app.service.impl.search;

import org.springframework.data.jpa.domain.Specification;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.persistence.criteria.*;

public class OrderSpecification {
    public static Specification<Order> search(String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate statusDel = criteriaBuilder.notEqual(root.get("statusForOrder"), StatusForOrder.DELETED);
            Predicate status = criteriaBuilder.like(root.get("statusForOrder").as(String.class), "%" + search + "%");
            Predicate name = criteriaBuilder.like(root.get("name"), "%" + search + "%");
            Predicate user = criteriaBuilder.like(root.get("user").get("customerDetails").get("fio"), "%" + search + "%");
            Join join = root.join("tags");
            Predicate tag = criteriaBuilder.like(join.get("tagName"), "%" + search + "%");
            Predicate description = criteriaBuilder.like(root.get("description"), "%" + search + "%");
            Predicate organizationName = criteriaBuilder.like(root.get("organizationName"), "%" + search + "%");
            Predicate dateStop = criteriaBuilder.like(root.get("dateStop").as(String.class), "%" + search + "%");
            Predicate number = criteriaBuilder.like(root.get("number").as(String.class), "%" + search + "%");
            Predicate mainPredicate = criteriaBuilder.or(description, organizationName, name, user, tag, dateStop,number, status);
            return criteriaBuilder.and(statusDel, mainPredicate);
        };
    }
}
