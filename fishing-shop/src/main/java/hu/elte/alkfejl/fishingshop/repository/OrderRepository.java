package hu.elte.alkfejl.fishingshop.repository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;

import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.model.QOrder;
import hu.elte.alkfejl.fishingshop.model.User;

/**
 * The OrderRepository interface handles database connection and transaction for
 * the Order entity. Extends from SoftDeletePagingRepository, which ensures that
 * orders can not be deleted, instead marked with an inactive flag.
 *
 * This repository uses QueryDsl: an extensive Java framework, which allows for
 * the generation of type-safe queries in a syntax similar to SQL.
 * QueryDslPredicateExecutor allows execution of Predicates, which can be used
 * to make complex, dynamic database queries.
 * 
 * QuerydslBinderCustomizer allows customizing the bindings of path parameters
 */

public interface OrderRepository extends SoftDeletePagingRepository<Order, Long>, QueryDslPredicateExecutor<Order>,
		QuerydslBinderCustomizer<QOrder> {

	// Finds an active/inactive order by their orderNumber
	Optional<Order> findByActiveAndOrderNumber(boolean active, long orderNumber);

	// Find a list of active/inactive orders by the user, who made the order
	List<Order> findByActiveAndUser(boolean active, User user);

	// Must be implemented as described by QuerydslBinderCustomizer
	@Override
	default public void customize(QuerydslBindings bindings, QOrder order) {
		/**
		 * Allows "between" search using the createDate property.
		 **
		 * Usage in URL parameter: ../search?createDate=2017.11.20&createDate=2017.11.22
		 * Order of the parameters are important! (earlier -> later) all means that
		 * every usage of the path parameter will be interpreted and iterated through by
		 * the iterator, but only the between of the first 2 values are returned by this
		 * method
		 */
		bindings.bind(order.createDate).all((path, value) -> {
			Iterator<? extends Date> it = value.iterator();
			return path.between(it.next(), it.next());
		});

		// Allows search by the id of the user, who made the order (exact value) (first
		// means that only the first usage of the path parameter will count)
		bindings.bind(order.user.id).first((path, value) -> path.eq(value));
		// Allows search in every String property of the Order class.
		// Returns true of the given string in the path is contained, not case sensitive
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
		// Allows search by order status (exact value as String)
		bindings.bind(order.status).first((EnumPath<Order.Status> path, Order.Status value) -> path.eq(value));
	}
}
