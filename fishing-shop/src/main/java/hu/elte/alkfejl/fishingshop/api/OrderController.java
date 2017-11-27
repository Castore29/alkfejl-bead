package hu.elte.alkfejl.fishingshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.annotation.Role;
import hu.elte.alkfejl.fishingshop.config.UserSession;
import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.service.OrderService;
import hu.elte.alkfejl.fishingshop.service.OrderService.OrderNotValidException;
import lombok.extern.slf4j.Slf4j;

import static hu.elte.alkfejl.fishingshop.model.User.Role.*;

/**
 * The OrderController handles incoming requests from the frontend regarding
 * orders. It uses the OrderService to compose a response.
 */

// Lombok annotation for easy logging
@Slf4j
// Spring annotation to mark this controller RESTful
@RestController
// Spring annotation to define the main endpoint of this controller for the
// dispatcher
@RequestMapping("/api/order")
// Enables CORS, so we can reach the backend through a browser and properly use
// cookies for session identification
@CrossOrigin("*")
public class OrderController {

	// Spring annotation to mark the orderService for dependency injection
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserSession userSession;

	// Custom annotation to mark which type of users can reach this endpoint (ADMIN
	// only in this case)
	@Role(ADMIN)
	// Maps to the /api/order/all endpoint for GET requests
	@GetMapping("/all")
	// This method can return every order in the database, but can be filtered and
	// paged by request parameters which is allowed by Predicate and Pageable.
	// Default page size is 20 and we set sort by createDate, descending order
	public ResponseEntity<Iterable<Order>> getOrders(@QuerydslPredicate(root = Order.class) Predicate predicate,
			@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(orderService.list(predicate, pageable));
	}

	@Role({ ADMIN, USER })
	@GetMapping("/orders")
	// This method returns only the logged in user's orders, if somehow a user is
	// not logged in (got through the AuthInterceptor), we send back the error
	// Can be filtered and paged exactly like in the previous method.
	public ResponseEntity<?> getOrdersByUser(@QuerydslPredicate(root = Order.class) Predicate predicate,
			@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
		if (userSession.isLoggedIn()) {
			return ResponseEntity.ok(orderService.listByUser(userSession.getLoggedInUser(), predicate, pageable));
		}
		log.warn("Nincs belépve felhasználó!");
		return ResponseEntity.badRequest().body("Nincs belépve felhasználó!");
	}

	@Role({ ADMIN, USER })
	@PostMapping("/save")
	// This method saves an order in the database and returns the saved value. The
	// logged in user automically becomes the user who made the order through this
	// endpoint. If any error happens during this, we send back the message
	public ResponseEntity<?> postOrder(@RequestBody Order order) {
		order.setUser(userSession.getLoggedInUser());
		try {
			return ResponseEntity.ok(orderService.createOrUpdate(order));
		} catch (OrderNotValidException e) {
			log.warn(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Role(ADMIN)
	@PostMapping("/update")
	// This method can update an order. It is meant to be used by an admin only to
	// manage the orders, e.g. updating their status
	public ResponseEntity<?> updateOrder(@RequestBody Order order) {
		try {
			return ResponseEntity.ok(orderService.createOrUpdate(order));
		} catch (OrderNotValidException e) {
			log.warn(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Role(ADMIN)
	@DeleteMapping("/delete")
	// This method deletes the given order, it should be rarely used, order
	// cancellation can be set by the status property
	public ResponseEntity<Order> deleteOrder(@RequestBody Order order) {
		orderService.delete(order);
		return ResponseEntity.ok().build();
	}

}
