package hu.elte.alkfejl.fishingshop.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import hu.elte.alkfejl.fishingshop.model.User;

/**
 * The UserSession represents the logged in user's data of a session. Basic
 * getter, setter, isLoggedIn() null-check method and logout() set-null method
 * provided
 */

// Spring annotation to mark UserSession as a Component
@Component
// Spring annotation to mark UserSession as a Component whose lifecycle is bound
// to the current web session.
@SessionScope
public class UserSession {

	private User user;

	public boolean isLoggedIn() {
		return this.user != null;
	}

	public void logout() {
		this.user = null;
	}

	public User getLoggedInUser() {
		return this.user;
	}

	public void setLoggedInUser(User user) {
		this.user = user;
	}

}
