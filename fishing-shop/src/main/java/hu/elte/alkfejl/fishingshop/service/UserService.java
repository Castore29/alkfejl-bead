package hu.elte.alkfejl.fishingshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.repository.UserRepository;

@Service
@SessionScope
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private User user;

	public User login(User user) throws UserNotValidException {
		if (isValid(user)) {
			return this.user = userRepository.findByActiveAndEmail(true, user.getEmail()).get();
		}
		throw new UserNotValidException();
	}

	public void logout() {
		user = null;
	}

	public User register(User user) {
		User oldUser = userRepository.findByActiveAndEmail(false, user.getEmail()).get();
		if (oldUser != null) {
			user.setId(oldUser.getId());
		}
		return this.user = userRepository.save(user);
	}

	public void deregister() {
		if (isLoggedIn()) {
			userRepository.delete(user);
			logout();
		}
	}

	public User update(User user) {
		return this.user = userRepository.save(user);
	}

	public boolean isValid(User user) {
		return userRepository.findByActiveAndEmail(true, user.getEmail()).isPresent();
	}

	public boolean isLoggedIn() {
		return user != null;
	}

	public User getLoggedInUser() {
		return user;
	}

	public class UserNotValidException extends Exception {
		private static final long serialVersionUID = 1L;
	}

}
