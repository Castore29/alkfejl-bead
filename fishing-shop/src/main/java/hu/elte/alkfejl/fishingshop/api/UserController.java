package hu.elte.alkfejl.fishingshop.api;

import static hu.elte.alkfejl.fishingshop.model.User.Role.ADMIN;
import static hu.elte.alkfejl.fishingshop.model.User.Role.GUEST;
import static hu.elte.alkfejl.fishingshop.model.User.Role.USER;

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
import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.service.UserService;
import hu.elte.alkfejl.fishingshop.service.UserService.UserNotValidException;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins="*")
public class UserController {

	@Autowired
	private UserService userService;

	@Role({ ADMIN, USER })
	@GetMapping
	public ResponseEntity<User> user() {
		if (userService.isLoggedIn()) {
			return ResponseEntity.ok(userService.getLoggedInUser());
		}
		return ResponseEntity.badRequest().build();
	}

	@Role(ADMIN)
	@GetMapping("/list")
	public ResponseEntity<Iterable<User>> getUsers(@QuerydslPredicate(root = User.class) Predicate predicate,
			@PageableDefault(sort = "email", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(userService.list(predicate, pageable));
	}

	@Role({ ADMIN, USER, GUEST })
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		return ResponseEntity.ok(userService.register(user));
	}

	@Role({ ADMIN, USER, GUEST })
	@DeleteMapping("/deregister")
	public ResponseEntity<User> deregister() {
		userService.deregister();
		return ResponseEntity.ok().build();
	}

	@Role({ ADMIN, USER, GUEST })
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.login(user));
		} catch (UserNotValidException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Role({ ADMIN, USER, GUEST })
	@PostMapping("/logout")
	public ResponseEntity<User> logout(@RequestBody User user) {
		userService.logout();
		return ResponseEntity.ok().build();
	}

}
