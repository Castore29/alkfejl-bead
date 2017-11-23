package hu.elte.alkfejl.fishingshop.service;

import java.lang.reflect.Field;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.config.UserSession;
import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.repository.UserRepository;

/**
 * The UserService service handles the connection between the UserController and
 * the UserRepository. The service's methods are called by the UserController
 * and the service calls the methods of the UserRepository. The service also
 * makes modifications on the incoming and outgoing data to keep up the
 * consistency and the business plan.
 */

// Spring annotation to mark UserService as a Service
@Service
public class UserService {

	// Spring annotation to mark the userRepository for dependency injection
	@Autowired
	private UserRepository userRepository;

	@Autowired
	// SessionScope component, which the stores the logged in user's data
	private UserSession userSession;

	// The login(User) method takes a User object and tries to login.
	public User login(User user) throws UserNotValidException {
		// we check if the user's email is registered, if not we throw an exception
		Optional<User> original = userRepository.findByActiveAndEmail(true, user.getEmail());
		if (original.isPresent()) {
			// we check if the given password is correct, if not we throw an exception
			if (!original.get().getPassword().equals(user.getPassword())) {
				throw new UserNotValidException("Hibás jelszó!");
			}
			// we use the userSession to login the user
			this.userSession.setLoggedInUser(original.get());
			return this.userSession.getLoggedInUser();
		}
		throw new UserNotValidException("A felhasználó nem létezik!");
	}

	// The list(Predicate, Pageable) method takes the given Predicate and Pageable
	// objects and lists every matching user, including the inactive users (if not
	// specified by the Predicate otherwise)
	public Iterable<User> list(Predicate predicate, Pageable pageable) {
		return userRepository.findAll(predicate, pageable);
	}

	// The register(User) method takes a User object and tries to register OR modify
	// the user
	public User register(User user) throws UserNotValidException {
		// we check if user exists
		Optional<User> original = userRepository.findByEmail(user.getEmail());
		if (original.isPresent()) {
			// check if email is active
			if (!original.get().isActive()) {
				// deactivated accounts cannot be reactivated
				throw new UserNotValidException("A törölt regisztrációt nem lehet újra aktiválni!");
			}
			// we check if the logged in user is the same as the user to-be-updated,
			// if not we throw an exception
			if (original.get().equals(userSession.getLoggedInUser())) {
				// update the null properties to the original's value
				user.setId(original.get().getId());
				update(user, original.get());
			} else {
				throw new UserNotValidException("Más felhasználó adatait nem lehet módosítani!");
			}
		}
		// we set the logged in user to the newly registered user
		this.userSession.setLoggedInUser(userRepository.save(user));
		return this.userSession.getLoggedInUser();
	}

	/**
	 * The update(User, User) takes the User to-be-updated and the original User and
	 * sets every null field of the to-be-updated User to the original's value using
	 * reflection. This method allows us to only send the modified properties in an
	 * update.
	 */
	private void update(User user, User original) {
		// we iterate through the declared fields of the User class
		// Note: the inherited fields are not included in this
		for (Field field : original.getClass().getDeclaredFields()) {
			// we set the field accessible for modification
			field.setAccessible(true);
			// we catch any exception thrown by reflection, but exceptions should not happen
			// if we make sure that both objects are of the User class
			try {
				// we check if the user's field is null
				if (field.get(user) == null) {
					// we set the user's null field to the original's value
					field.set(user, field.get(original));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// we make sure that the properties inherited from BaseEntity are also set
		// correctly
		user.setVersion(original.getVersion());
		user.setCreateDate(original.getCreateDate());
		user.setActive(true);
	}

	// The deactivate() method simply soft deletes the logged in user
	public void deactivate() {
		userRepository.delete(this.userSession.getLoggedInUser());
		this.userSession.logout();
	}

	// Nested class for custom exception
	public class UserNotValidException extends Exception {
		private static final long serialVersionUID = -2434584274378290996L;

		public UserNotValidException(String msg) {
			super(msg);
		}
	}

}
