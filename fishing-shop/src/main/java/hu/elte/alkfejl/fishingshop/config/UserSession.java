package hu.elte.alkfejl.fishingshop.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import hu.elte.alkfejl.fishingshop.model.User;

@Component
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
