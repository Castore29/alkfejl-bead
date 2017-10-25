package hu.elte.alkfejl.fishingshop.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import hu.elte.alkfejl.fishingshop.model.Product;

public interface ProductRepository
		extends SoftDeleteCrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {

	Optional<Product> findByActiveAndItemNumber(boolean active, long itemNumber);

}
