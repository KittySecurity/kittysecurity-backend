package pl.edu.pk.student.kittysecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pk.student.kittysecurity.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByDisplayName(String username);

    Optional<User> findByEmail(String email);

}
