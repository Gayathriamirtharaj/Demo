package repository;
import org.springframework.data.jpa.repository.JpaRepository;

import model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUsername(String username);

    boolean existsById(int id);

    UserEntity findByUsername(String username);

    UserEntity findByLastName(String lastName);

}