package hu.elte.alkfejl.fishingshop.repository;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.*;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.querydsl.core.types.dsl.StringPath;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QProduct;

public interface ProductRepository
		extends SoftDeleteCrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long>,
		QueryDslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {

	Optional<Product> findByActiveAndItemNumber(boolean active, long itemNumber);

	@Query("select distinct p.category as category from Product p")
	Iterable<String> findCategories();

	@Query("select distinct p.subCategory as subCategory from Product p where p.category=?1")
	Iterable<String> findSubCategories(String category);

	Iterable<Product> findByCategory(String category);

	Page<Product> findByCategoryAndSubCategory(String category, String subCategory, Pageable pageable);

	@Override
	default public void customize(QuerydslBindings bindings, QProduct product) {

		bindings.bind(product.price).all((path, value) -> {
			Iterator<? extends Integer> it = value.iterator();
			return path.between(it.next(), it.next());
		});

		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
		bindings.excluding(product.image);
		bindings.excluding(product.orders);

	}

}
