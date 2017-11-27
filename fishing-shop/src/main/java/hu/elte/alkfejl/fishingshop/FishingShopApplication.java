package hu.elte.alkfejl.fishingshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * SpringBoot application starter class. Run this to start app.
 */

// Convenience annotation for every needed configuration in a starter class
@SpringBootApplication
public class FishingShopApplication extends WebMvcConfigurerAdapter {

	// Spring annotation to mark the authInterceptor for dependency injection
	@Autowired
	private HandlerInterceptor authInterceptor;

	// we add our custom AuthInterceptor to the InterceptorRegistry, so it will be
	// used by Spring
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor);
	}

	public static void main(String[] args) {
		SpringApplication.run(FishingShopApplication.class, args);
	}
}
