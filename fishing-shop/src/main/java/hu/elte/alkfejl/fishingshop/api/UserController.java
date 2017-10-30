package hu.elte.alkfejl.fishingshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.service.UserService;
import hu.elte.alkfejl.fishingshop.service.UserService.UserNotValidException;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<User> user() {
		if (userService.isLoggedIn()) {
			return ResponseEntity.ok(userService.getLoggedInUser());
		}
		return ResponseEntity.badRequest().build();
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		return ResponseEntity.ok(userService.register(user));
	}

	@DeleteMapping("/deregister")
	public ResponseEntity<User> deregister() {
		userService.deregister();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.login(user));
		} catch (UserNotValidException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<User> logout(@RequestBody User user) {
		userService.logout();
		return ResponseEntity.ok().build();
	}

}
