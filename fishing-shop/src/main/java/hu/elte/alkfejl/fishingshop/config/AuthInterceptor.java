package hu.elte.alkfejl.fishingshop.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import hu.elte.alkfejl.fishingshop.annotation.Role;
import hu.elte.alkfejl.fishingshop.model.User;

/**
 * The AuthInterceptor class intercepts every request and checks if the request
 * has correct authorization
 */

// Spring annotation to mark UserSession as a Component
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

	// Spring annotation to mark the userSession for dependency injection
	@Autowired
	private UserSession userSession;

	// Must be implemented due to HandlerInterceptorAdapter
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// if the handler is not a HandlerMethod object, then it is (most certainly) a
		// PreFlightHandler, which is used during CORS. We don't want to intercept this
		// request, so we let the browser and the backend communicate the session data
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		List<User.Role> routeRoles = getRoles((HandlerMethod) handler);
		// when there are no restrictions, we let the user through
		if (routeRoles.isEmpty() || routeRoles.contains(User.Role.GUEST)) {
			return true;
		}
		// check role
		if (userSession.isLoggedIn() && routeRoles.contains(userSession.getLoggedInUser().getRole())) {
			return true;
		}
		response.setStatus(401);
		return false;
	}

	// we retrieve the allowed Roles attached to the requested endpoint
	private List<User.Role> getRoles(HandlerMethod handler) {
		Role role = handler.getMethodAnnotation(Role.class);
		return role == null ? Collections.emptyList() : Arrays.asList(role.value());
	}

}
