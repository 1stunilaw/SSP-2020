package ssp.marketplace.app.repository;

import com.querydsl.core.types.dsl.*;
import lombok.NonNull;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.*;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.entity.statuses.*;

import java.time.*;
import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID>,
        QuerydslPredicateExecutor<Order>, QuerydslBinderCustomizer<QOrder>
{
    @Override
    default void customize(@NonNull QuerydslBindings bindings, @NonNull QOrder root) {
        // Сделать фильтр 'like' без учета регистра для всех свойств строки
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>)StringExpression::containsIgnoreCase);

        //Добавить свойство даты фильтра "между" и "больше или равно"
        bindings.bind(root.dateStart, root.dateStop).all((path, value) -> {
            Iterator<? extends LocalDateTime> it = value.iterator();
            LocalDateTime from = it.next();
            if (value.size() >= 2) {
                LocalDateTime to = it.next();
                return Optional.of(path.between(from, to)); // between
            } else {
                return Optional.of(path.goe(from)); // greater than or equal
            }
        });
    }

    Page<Order> findByStatusForOrderNotIn(Pageable pageable, final Collection<StatusForOrder> statusForOrder);

    Optional<Order> findByIdAndStatusForOrderNotIn(UUID id,
            final Collection<StatusForOrder> statusForOrder);

    Optional<Order> findByName(String name);
    @Query(value = "select number from orders where name = ?1", nativeQuery = true) //// TODO: 23.11.2020 исправить получение номера
    Long getNumber(String name);
}
