package hu.elte.alkfejl.fishingshop.repository;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.querydsl.core.types.dsl.StringPath;

import hu.elte.alkfejl.fishingshop.model.QUser;
import hu.elte.alkfejl.fishingshop.model.User;

/**
 * The UserRepository interface handles database connection and transaction for
 * the User entity. Extends from SoftDeletePagingRepository, which ensures that
 * orders can not be deleted, instead marked with an inactive flag.
 *
 * This repository uses QueryDsl: an extensive Java framework, which allows for
 * the generation of type-safe queries in a syntax similar to SQL.
 * QueryDslPredicateExecutor allows execution of Predicates, which can be used
 * to make complex, dynamic database queries.
 * 
 * QuerydslBinderCustomizer allows customizing the bindings of path parameters
 */

public interface UserRepository extends SoftDeletePagingRepository<User, Long>, QueryDslPredicateExecutor<User>,
		QuerydslBinderCustomizer<QUser> {
	
	// Finds a user by their email
	Optional<User> findByEmail(String email);

	// Finds an active/inactive user by their email
	Optional<User> findByActiveAndEmail(boolean active, String email);

	// Finds an active/inactive user by their email AND password
	Optional<User> findByActiveAndEmailAndPassword(boolean active, String email, String password);

	// Must be implemented as described in QuerydslBinderCustomizer
	@Override
	default public void customize(QuerydslBindings bindings, QUser user) {
		/**
		 * Allows "between" search using the createDate property.
		 **
		 * Usage in URL parameter: ../search?createDate=2017.11.20&createDate=2017.11.22
		 * Order of the parameters are important! (earlier -> later) all means that
		 * every usage of the path parameter will be interpreted and iterated through by
		 * the iterator, but only the between of the first 2 values are returned by this
		 * method
		 */
		bindings.bind(user.createDate).all((path, value) -> {
			Iterator<? extends Date> it = value.iterator();
			return path.between(it.next(), it.next());
		});

		// Allows search in every String property of the Order class.
		// Returns true of the given string in the path is contained, not case sensitive
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));

		// password should not be searched by QueryDsl
		bindings.excluding(user.password);
	}

}
