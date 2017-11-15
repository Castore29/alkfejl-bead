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

public interface UserRepository extends SoftDeletePagingRepository<User, Long>, QueryDslPredicateExecutor<User>,
		QuerydslBinderCustomizer<QUser> {

	Optional<User> findByActiveAndEmail(boolean active, String email);
	
	@Override
	default public void customize(QuerydslBindings bindings, QUser user) {
		bindings.bind(user.createDate).all((path, value) -> {
			Iterator<? extends Date> it = value.iterator();
			return path.between(it.next(), it.next());
		});

		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
		
		bindings.excluding(user.password);
	}

}
