package pl.edu.pk.student.kittysecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.student.kittysecurity.entity.PasswordEntry;
import pl.edu.pk.student.kittysecurity.entity.User;

import java.util.List;


public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Integer> {

    List<PasswordEntry> findByUser(User user);
}
