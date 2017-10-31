package hu.elte.alkfejl.fishingshop.repository;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import com.querydsl.core.types.dsl.StringPath;

import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.model.QOrder;
import hu.elte.alkfejl.fishingshop.model.User;

public interface OrderRepository extends SoftDeletePagingRepository<Order, Long>, QueryDslPredicateExecutor<Order>,
		QuerydslBinderCustomizer<QOrder> {

	Optional<Order> findByActiveAndOrderNumber(boolean active, long orderNumber);

	Iterable<Order> findByActiveAndUser(boolean active, User user);

	@Override
	default public void customize(QuerydslBindings bindings, QOrder order) {
		bindings.bind(order.createDate).all((path, value) -> {
			Iterator<? extends Date> it = value.iterator();
			return path.between(it.next(), it.next());
		});

		bindings.bind(order.user.id).first((path, value) -> path.eq(value));
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
		bindings.bind(order.status).first((path, value) -> path.eq(value));
	}

}
