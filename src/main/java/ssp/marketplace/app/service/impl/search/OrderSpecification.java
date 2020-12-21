package ssp.marketplace.app.service.impl.search;

import org.springframework.data.jpa.domain.Specification;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.persistence.criteria.*;

public class OrderSpecification {

    public static Specification<Order> search(String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate statusDel = criteriaBuilder.notEqual(root.get("statusForOrder"), StatusForOrder.DELETED);
            Predicate name = criteriaBuilder.like(root.get("name"), "%" + search + "%");
            Predicate user = criteriaBuilder.like(root.get("user").get("customerDetails").get("fio"), "%" + search + "%");
            Predicate tag = criteriaBuilder.like(criteriaBuilder.lower(root.join("tags", JoinType.LEFT).get("tagName")), "%" + search + "%");
            Predicate description = criteriaBuilder.like(root.get("description"), "%" + search + "%");
            Predicate organizationName = criteriaBuilder.like(root.get("organizationName"), "%" + search + "%");
            Predicate number = criteriaBuilder.like(root.get("number").as(String.class), "%" + search + "%");
            Predicate mainPredicate = criteriaBuilder.or(description, organizationName, name, user, number, tag);
            return criteriaBuilder.and(statusDel, mainPredicate);
        };
    }

    public static Specification<Order> statusFilter(String status) {
        return (root, query, criteriaBuilder) -> {
            Predicate filterByStatus = criteriaBuilder.equal(root.get("statusForOrder").as(String.class), status);
            return filterByStatus;
        };
    }
}
