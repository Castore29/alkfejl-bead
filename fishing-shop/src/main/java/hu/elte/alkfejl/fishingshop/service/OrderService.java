package hu.elte.alkfejl.fishingshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QOrder;

import static hu.elte.alkfejl.fishingshop.model.Order.Status.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.repository.OrderRepository;
import hu.elte.alkfejl.fishingshop.repository.ProductRepository;
import hu.elte.alkfejl.fishingshop.repository.UserRepository;

/**
 * The OrderService service handles the connection between the OrderController
 * and the OrderRepository. The service's methods are called by the
 * OrderController and the service calls the methods of the OrderRepository AND
 * the ProductRepository AND the UserRepository (for validation purposes). The
 * service also makes modifications on the incoming and outgoing data to keep up
 * the consistency and the business plan.
 */

// Spring annotation to mark OrderService as a Service
@Service
public class OrderService {

	// Spring annotation to mark the orderRepository for dependency injection
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	// The createOrUpdate(Order) method handles order creation and modification
	@Transactional
	public Order createOrUpdate(Order order) throws OrderNotValidException {
		order.setUser(validateUser(order.getUser()));
		// if no products are ordered, the order is invalid
		if (order.getProducts().isEmpty()) {
			throw new OrderNotValidException("Nincs rendelt termék!");
		}

		// we create a helper map to easily iterate over the same products and their
		// ordered amounts
		Map<Product, Integer> productMap = new HashMap<>();
		for (Product product : order.getProducts()) {
			if (productMap.containsKey(product)) {
				productMap.put(product, productMap.get(product) + 1);
			} else {
				product = validateProduct(product);
				productMap.put(product, 1);
			}
		}

		Optional<Order> original = orderRepository.findById(order.getId());
		if (original.isPresent()) {
			update(order, original.get());
			// if the order already exists in the database we check their status
			if (original.get().getStatus() != CANCELLED && order.getStatus() == CANCELLED) {
				// if the order becomes CANCELLED, it means that the reserved products should be
				// put back into stock
				for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
					entry.getKey().setStock(entry.getKey().getStock() + entry.getValue());
				}
				productRepository.save(productMap.keySet());
			}
		} else {
			// we set the orderNumber by concatenating the user ID with the number of orders
			// the user had
			order.setOrderNumber(
					order.getUser().getId() + "-" + orderRepository.findByActiveAndUser(true, order.getUser()).size());
			// if the order is new, its status should become RECEIVED
			order.setStatus(RECEIVED);
			// recalculate the total price for the order
			order.setTotal(0);
			// iterate over a map of products and their ordered amount
			for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
				order.setTotal(order.getTotal() +
						(entry.getKey().getPrice() - entry.getKey().getPrice() * entry.getKey().getDiscount() / 100)
						* entry.getValue());
				// if the order is RECEIVED, we reduce the stock amount by the ordered amount of
				// the same product
				entry.getKey().setStock(entry.getKey().getStock() - entry.getValue());
			}
			productRepository.save(productMap.keySet());
		}

		// save the order through the repository
		return orderRepository.save(order);
	}

	// The validateProduct(Product) method makes sure that a product's every
	// property stays the same, so an order can not change it
	private Product validateProduct(Product product) throws OrderNotValidException {
		Optional<Product> originalProduct = productRepository.findById(product.getId());
		if (originalProduct.isPresent()) {
			// if the product exists in the database, we set the ordered product's
			// properties accordingly
			return originalProduct.get();
		} else {
			// if the id of the product is not valid, the order is invalid
			throw new OrderNotValidException("A választott termék nem elérhető! ID: " + product.getId());
		}
	}

	// The validateUser(User) method makes sure that a user's every
	// property stays the same, so an order can not change it
	private User validateUser(User user) throws OrderNotValidException {
		Optional<User> originalUser = userRepository.findById(user.getId());
		if (originalUser.isPresent()) {
			// if the user exists in the database, we set the user
			// properties accordingly
			return originalUser.get();
		} else {
			// if the id of the user is not valid, the order is invalid
			throw new OrderNotValidException("Nem létező felhasználó! ID: " + user.getId());
		}
	}

	/**
	 * The update(Order, Order) takes the Order to-be-updated and the original Order
	 * and sets every null field of the to-be-updated Product to the original's
	 * value using reflection. This method allows us to only send the modified
	 * properties in an update.
	 */
	private void update(Order order, Order original) {
		// we iterate through the declared fields of the Order class
		// Note: the inherited fields are not included in this
		for (Field field : original.getClass().getDeclaredFields()) {
			// we set the field accessible for modification
			field.setAccessible(true);
			// we catch any exception thrown by reflection, but exceptions should not happen
			// if we make sure that both objects are of the Order class
			try {
				// we check if the order's field is null
				if (field.get(order) == null) {
					// we set the product's null field to the original's value
					field.set(order, field.get(original));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// we make sure that the properties inherited from BaseEntity are also set
		// correctly
		order.setVersion(original.getVersion());
		order.setCreateDate(original.getCreateDate());
		order.setActive(true);
	}

	// The delete(Order) method simply soft deletes a given order
	public void delete(Order order) {
		orderRepository.delete(order);
	}

	// The listDeleted() method lists all of the soft deleted orders
	public Iterable<Order> listDeleted() {
		return orderRepository.findInactive();
	}

	// The listByUser(User, Predicate, Pageable) method takes the given user and
	// lists all of their orders with the given Predicate and Pageable objects
	public Iterable<Order> listByUser(User user, Predicate predicate, Pageable pageable) {
		// make a new Predicate, adding the user's id to it
		predicate = ExpressionUtils.allOf(predicate, QOrder.order.user.id.eq(user.getId()));
		return list(predicate, pageable);
	}

	// The list(Predicate, Pageable) method takes the given Predicate and Pageable
	// objects and lists every matching order (regardless of User, if not specified
	// by the
	// Predicate)
	public Iterable<Order> list(Predicate predicate, Pageable pageable) {
		// make a new Predicate, adding the active property to it as true
		predicate = ExpressionUtils.allOf(predicate, QOrder.order.active.eq(true));
		return orderRepository.findAll(predicate, pageable);
	}

	// Nested class for custom exception
	public class OrderNotValidException extends Exception {
		private static final long serialVersionUID = 1994069544504067141L;

		public OrderNotValidException(String msg) {
			super(msg);
		}

	}

}
