package hu.elte.alkfejl.fishingshop.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.config.UserSession;
import hu.elte.alkfejl.fishingshop.model.User;
import hu.elte.alkfejl.fishingshop.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSession userSession;

	public User login(User user) throws UserNotValidException {
		Optional<User> original = userRepository.findByActiveAndEmail(true, user.getEmail());
		if (original.isPresent()) {
			this.userSession.setLoggedInUser(original.get());
			return this.userSession.getLoggedInUser();
		}
		throw new UserNotValidException();
	}

	public Iterable<User> list(Predicate predicate, Pageable pageable) {
		return userRepository.findAll(predicate, pageable);
	}

	public User register(User user) {
		Optional<User> oldUser = userRepository.findByEmail(user.getEmail());
		if (oldUser.isPresent()) {
			user.setId(oldUser.get().getId());
			user.setCreateDate(oldUser.get().isActive() ? oldUser.get().getCreateDate() : new Date());
			user.setActive(true);
			user.setVersion(oldUser.get().getVersion());
		}
		this.userSession.setLoggedInUser(userRepository.save(user));
		return this.userSession.getLoggedInUser();
	}

	public void deregister() {
		userRepository.delete(this.userSession.getLoggedInUser());
		this.userSession.logout();
	}

	public class UserNotValidException extends Exception {
		private static final long serialVersionUID = 1L;
	}

}
