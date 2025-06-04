package pl.edu.pk.student.kittysecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.student.kittysecurity.entity.PasswordGenSettings;

public interface PasswordGenSettingsRepository extends JpaRepository<PasswordGenSettings, Long> {

}
