package hu.elte.alkfejl.fishingshop.repository;

import java.util.Optional;

import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.model.User;

public interface OrderRepository extends SoftDeleteCrudRepository<Order, Long> {

	Optional<Order> findByActiveAndOrderNumber(boolean active, long orderNumber);

	Iterable<Order> findByActiveAndUser(boolean active, User user);

}
