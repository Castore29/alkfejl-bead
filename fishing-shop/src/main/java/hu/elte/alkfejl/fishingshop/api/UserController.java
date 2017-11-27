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
import hu.elte.alkfejl.fishingshop.config.UserSession;
import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.service.UserService;
import hu.elte.alkfejl.fishingshop.service.UserService.UserNotValidException;
import lombok.extern.slf4j.Slf4j;

/**
 * The UserController handles incoming requests from the frontend regarding
 * users. It uses the UserService to compose a response.
 */

// Lombok annotation for easy logging
@Slf4j
// Spring annotation to mark this controller RESTful
@RestController
// Spring annotation to define the main endpoint of this controller for the
// dispatcher
@RequestMapping("/api/user")
// Enables CORS, so we can reach the backend through a browser and properly use
// cookies for session identification
@CrossOrigin("*")
public class UserController {

	// Spring annotation to mark the orderService for dependency injection
	@Autowired
	private UserService userService;

	@Autowired
	private UserSession userSession;

	// Custom annotation to mark which type of users can reach this endpoint (ADMIN
	// and USER only in this case)
	@Role({ ADMIN, USER })
	// Maps to the /api/order endpoint for GET requests
	@GetMapping
	// This method returns the currently logged in user, if somehow a user is
	// not logged in (got through the AuthInterceptor), we send back the error
	public ResponseEntity<User> user() {
		if (userSession.isLoggedIn()) {
			return ResponseEntity.ok(userSession.getLoggedInUser());
		}
		return ResponseEntity.badRequest().build();
	}

	@Role(ADMIN)
	@GetMapping("/list")
	// This method returns a list of all users with optional pagination and QueryDsl
	// search parameters. It is ADMIN only
	public ResponseEntity<Iterable<User>> getUsers(@QuerydslPredicate(root = User.class) Predicate predicate,
			@PageableDefault(sort = "email", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(userService.list(predicate, pageable));
	}

	@Role({ ADMIN, USER, GUEST })
	@PostMapping("/login")
	// This method logs in the given user, if it is not a valid user, we return the
	// error message
	public ResponseEntity<?> login(@RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.login(user));
		} catch (UserNotValidException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@Role({ ADMIN, USER })
	@PostMapping("/logout")
	// This method logs out the logged in user and returns with no content status
	public ResponseEntity<User> logout() {
		userSession.logout();
		return ResponseEntity.status(204).build();
	}

	@Role({ ADMIN, USER, GUEST })
	@PostMapping("/register")
	// This method registers a new user or updates the currently logged in user.
	// If any error occurs, we return the error message
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.register(user));
		} catch (UserNotValidException e) {
			log.warn(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@Role({ ADMIN, USER })
	@DeleteMapping("/deactivate")
	// This method soft deletes and logs out the currently logged in user. Should be
	// cautiously used, because currently users can not be reactivated
	public ResponseEntity<?> deactivate() {
		userService.deactivate();
		return ResponseEntity.status(204).build();
	}

}
