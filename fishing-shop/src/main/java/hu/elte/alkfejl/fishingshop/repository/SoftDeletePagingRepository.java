package hu.elte.alkfejl.fishingshop.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import hu.elte.alkfejl.fishingshop.model.BaseEntity;

@NoRepositoryBean
public interface SoftDeletePagingRepository<T extends BaseEntity, ID extends Long>
		extends PagingAndSortingRepository<T, ID> {

	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.active = true")
	Iterable<T> findAll();

	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.id in ?1 and e.active = true")
	Iterable<T> findAll(Iterable<ID> ids);

	@Override
	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e where e.id = ?1 and e.active = true")
	T findOne(ID id);

	@Query("select e from #{#entityName} e where e.active = false")
	@Transactional(readOnly = true)
	Iterable<T> findInactive();

	@Override
	@Transactional(readOnly = true)
	@Query("select count(e) from #{#entityName} e where e.active = true")
	long count();

	@Override
	@Transactional(readOnly = true)
	default boolean exists(ID id) {
		return findOne(id) != null;
	}

	@Override
	@Query("update #{#entityName} e set e.active=false where e.id = ?1")
	@Transactional
	@Modifying
	void delete(Long id);

	@Override
	@Transactional
	default void delete(T entity) {
		delete(entity.getId());
	}

	@Override
	@Transactional
	default void delete(Iterable<? extends T> entities) {
		entities.forEach(entitiy -> delete(entitiy.getId()));
	}

	@Override
	@Query("update #{#entityName} e set e.active=false")
	@Transactional
	@Modifying
	void deleteAll();

}
