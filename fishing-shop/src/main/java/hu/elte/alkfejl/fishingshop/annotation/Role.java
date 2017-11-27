package hu.elte.alkfejl.fishingshop.annotation;

import hu.elte.alkfejl.fishingshop.model.User;

import java.lang.annotation.*;

/**
 * Custom annotation used to mark endpoints in the controllers with Role-based
 * authorization
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Role {
	User.Role[] value() default { User.Role.GUEST };
}
