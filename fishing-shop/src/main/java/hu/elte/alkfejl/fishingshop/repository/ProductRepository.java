package hu.elte.alkfejl.fishingshop.repository;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.*;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.querydsl.core.types.dsl.StringPath;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QProduct;

/**
 * The ProductRepository interface handles database connection and transaction
 * for the Product entity. Extends from SoftDeletePagingRepository, which
 * ensures that products can not be deleted, instead marked with an inactive
 * flag.
 *
 * This repository uses QueryDsl: an extensive Java framework, which allows for
 * the generation of type-safe queries in a syntax similar to SQL.
 * QueryDslPredicateExecutor allows execution of Predicates, which can be used
 * to make complex, dynamic database queries.
 * 
 * QuerydslBinderCustomizer allows customizing the bindings of path parameters
 */

public interface ProductRepository extends SoftDeletePagingRepository<Product, Long>,
		QueryDslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {

	// Finds an active/inactive product by their productNumber
	Optional<Product> findByActiveAndItemNumber(boolean active, long itemNumber);

	// Custom query to get all possible categories and their subcategories.
	// Returns an Iterable of 2-sized Object arrays (Object[0] category, Object[1]
	// subCategory)
	@Query("select distinct p.category as category, p.subCategory as subCategory from Product p")
	Iterable<Object[]> findCategories();

	// Finds a list of products by category
	Iterable<Product> findByCategory(String category);

	// Returns a Page of products by category and subCategory
	// Should be used for basic item listing
	Page<Product> findByCategoryAndSubCategory(String category, String subCategory, Pageable pageable);

	// Must be implemented as described in QuerydslBinderCustomizer
	@Override
	default public void customize(QuerydslBindings bindings, QProduct product) {
		/**
		 * Allows "between" search using the price property.
		 **
		 * Usage in URL parameter: ../search?price=1000&price=2000
		 **
		 * Order of the parameters are important! (earlier -> later) all means that
		 * every usage of the path parameter will count and iterated through by the
		 * iterator, but only the between of the first 2 values are returned by this
		 * method
		 */
		bindings.bind(product.price).all((path, value) -> {
			Iterator<? extends Integer> it = value.iterator();
			return path.between(it.next(), it.next());
		});

		// Allows search by availability, e.g. ../search?available=true (first means
		// that only the first usage of the path parameter will count)
		bindings.bind(product.available).first((path, value) -> path.eq(value));
		// Allows search by discount, e.g. ../search?discount=20 (path.goe(value)
		// returns greater or equal values than the one given in the path parameter)
		bindings.bind(product.discount).first((path, value) -> path.goe(value));
		// Allows search by stock, e.g. ../search?stock=20 (path.goe(value)
		// returns greater or equal values than the one given in the path parameter)
		bindings.bind(product.stock).first((path, value) -> path.goe(value));

		// Allows search in every String property of the Order class.
		// Returns true of the given string in the path is contained, not case sensitive
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));

		// excluding means these properties can not be searched by with QueryDsl
		bindings.excluding(product.image);
		bindings.excluding(product.orders);

	}

}
