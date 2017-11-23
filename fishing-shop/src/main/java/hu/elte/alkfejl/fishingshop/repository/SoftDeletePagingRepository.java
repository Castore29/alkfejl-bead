package hu.elte.alkfejl.fishingshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import hu.elte.alkfejl.fishingshop.model.BaseEntity;

/**
 * The SoftDeletePagingRepository is the superclass of all used repositories.
 * Supports every entity, which extends from BaseEntity. Extends from
 * PagingAndSortingRepository, which is an enhanced CrudRepository for
 * retrieving data from database sorted and paged. All custom query are designed
 * with the soft delete aspect in mind, i.e. the active property is taken into
 * account in every query.
 */

// Spring annotation to mark this repository so it should not be created
// directly as a Bean
@NoRepositoryBean
public interface SoftDeletePagingRepository<T extends BaseEntity, ID extends Long>
		extends PagingAndSortingRepository<T, ID> {

	@Override
	// Spring annotation to mark this method as a transaction and that it will only
	// read from the database
	@Transactional(readOnly = true)
	// Custom query to find all of the active entities
	@Query("select e from #{#entityName} e where e.active = true")
	Iterable<T> findAll();

	@Override
	@Transactional(readOnly = true)
	// Custom query to find all of the active entities given their ids
	@Query("select e from #{#entityName} e where e.id in ?1 and e.active = true")
	Iterable<T> findAll(Iterable<ID> ids);

	@Transactional(readOnly = true)
	// Custom query to find one active entity given their id
	@Query("select e from #{#entityName} e where e.id = ?1 and e.active = true")
	Optional<T> findById(ID id);

	@Transactional(readOnly = true)
	// Custom query to find all inactive entities
	@Query("select e from #{#entityName} e where e.active = false")
	Iterable<T> findInactive();

	@Override
	@Transactional(readOnly = true)
	// Custom query to count all active entities
	@Query("select count(e) from #{#entityName} e where e.active = true")
	long count();

	@Override
	@Transactional(readOnly = true)
	// Returns true if an entity exists with the given id
	default boolean exists(ID id) {
		return findOne(id) != null;
	}

	@Override
	// Custom query to soft delete an entity by setting their active property to
	// false given their id
	@Query("update #{#entityName} e set e.active=false where e.id = ?1")
	@Transactional
	// Indicates a method should be regarded as modifying query
	@Modifying
	void delete(Long id);

	@Override
	@Transactional
	// Overloaded method for soft delete given the whole entity
	default void delete(T entity) {
		delete(entity.getId());
	}

	@Override
	@Transactional
	// Overloaded method for soft delete given an Iterable of entities
	default void delete(Iterable<? extends T> entities) {
		entities.forEach(entitiy -> delete(entitiy.getId()));
	}

	@Override
	// Custom query to soft delete ALL entities by setting their active property to
	// false
	@Query("update #{#entityName} e set e.active=false")
	@Transactional
	// Indicates a method should be regarded as modifying query
	@Modifying
	void deleteAll();

}
