package hu.elte.alkfejl.fishingshop.repository;

import org.springframework.data.repository.CrudRepository;

import hu.elte.alkfejl.fishingshop.model.Tag;

public interface TagRepository extends CrudRepository<Tag, String> {

}
