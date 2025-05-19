package pl.edu.pk.student.kittysecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.student.kittysecurity.entity.RefreshToken;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    List<RefreshToken> findAllByPrefix(String tokenPrefix);
}