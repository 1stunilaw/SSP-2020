package ssp.marketplace.app.service.impl.search;

import org.springframework.data.jpa.domain.Specification;
import ssp.marketplace.app.entity.Order;
import ssp.marketplace.app.entity.statuses.StatusForOrder;

import javax.persistence.criteria.*;

public class OrderSpecification {

    public static Specification<Order> search(String search) {
        return (root, query, criteriaBuilder) -> {
            String likeSearch = "%" + search + "%";
            Predicate statusDel = criteriaBuilder.notEqual(root.get("statusForOrder"), StatusForOrder.DELETED);
            Predicate name = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeSearch.toLowerCase());
            Predicate user = criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("customerDetails").get("fio")),  likeSearch.toLowerCase());
            Predicate tag = criteriaBuilder.like(criteriaBuilder.lower(root.join("tags", JoinType.LEFT).get("tagName")),  likeSearch.toLowerCase());
            Predicate description = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),  likeSearch.toLowerCase());
            Predicate organizationName = criteriaBuilder.like(criteriaBuilder.lower(root.get("organizationName")),  likeSearch.toLowerCase());
            Predicate number = criteriaBuilder.like(criteriaBuilder.lower(root.get("number").as(String.class)),  likeSearch.toLowerCase());
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
