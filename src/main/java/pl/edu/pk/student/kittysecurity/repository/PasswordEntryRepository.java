package pl.edu.pk.student.kittysecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.student.kittysecurity.entity.PasswordEntry;


public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Integer> {

}
