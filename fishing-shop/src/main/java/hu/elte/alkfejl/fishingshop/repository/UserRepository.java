package hu.elte.alkfejl.fishingshop.repository;

import java.util.Optional;

import hu.elte.alkfejl.fishingshop.model.User;

public interface UserRepository extends SoftDeleteCrudRepository<User, Long> {

	Optional<User> findByActiveAndEmail(boolean active, String email);

}
