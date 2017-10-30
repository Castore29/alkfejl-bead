package hu.elte.alkfejl.fishingshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.alkfejl.fishingshop.annotation.Role;
import hu.elte.alkfejl.fishingshop.model.Order;
import hu.elte.alkfejl.fishingshop.service.OrderService;
import hu.elte.alkfejl.fishingshop.service.UserService;

import static hu.elte.alkfejl.fishingshop.model.User.Role.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Role(ADMIN)
	@GetMapping("/all")
	public ResponseEntity<Iterable<Order>> getOrders() {
		return ResponseEntity.ok(orderService.list());
	}

	@Role({ ADMIN, USER })
	@GetMapping("/myOrders")
	public ResponseEntity<Iterable<Order>> getOrdersByUser() {
		if (userService.isLoggedIn()) {
			return ResponseEntity.ok(orderService.listByUser(userService.getLoggedInUser()));
		}
		return ResponseEntity.badRequest().build();
	}

	@Role({ ADMIN, USER })
	@PostMapping("/add")
	public ResponseEntity<Order> postOrder(@RequestBody Order order) {
		order.setUser(userService.getLoggedInUser());
		return ResponseEntity.ok(orderService.createOrUpdate(order));
	}

	@Role(ADMIN)
	@PostMapping("/update")
	public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
		return ResponseEntity.ok(orderService.createOrUpdate(order));
	}

	@Role(ADMIN)
	@DeleteMapping("/delete")
	public ResponseEntity<Order> deleteOrder(@RequestBody Order order) {
		orderService.delete(order);
		return ResponseEntity.ok().build();
	}

}
