package pl.edu.pk.student.kittysecurity.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsGetResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.entity.PasswordGenSettings;
import pl.edu.pk.student.kittysecurity.repository.PasswordGenSettingsRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class PasswordGenSettingsService {

    private final AuthContextService authContextService;
    private final PasswordGenSettingsRepository passwordGenSettingsRepository;

    public PasswordGenSettingsService(AuthContextService authContextService, PasswordGenSettingsRepository passwordGenSettingsRepository) {
        this.authContextService = authContextService;
        this.passwordGenSettingsRepository = passwordGenSettingsRepository;
    }

    public ResponseEntity<StatusResponseDto> updatePasswordGenSettingsEntity(String jwtToken, PasswordGenSettingsUpdateRequestDto request) {
        int calculatedPasswordLength = calculatePasswordLength(request);

        if(calculatedPasswordLength > request.getPasswordLength()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Given configuration exceeds password length!");
        }

        Long userId = authContextService.extractUserIdFromToken(jwtToken);

        Optional<PasswordGenSettings> passwordGenSettings = passwordGenSettingsRepository.findById(userId);

        PasswordGenSettings foundSettings = updatePasswordGenSettingsEntity(request, passwordGenSettings);

        passwordGenSettingsRepository.save(foundSettings);

        return ResponseEntity.ok().body(StatusResponseDto.builder()
                .status("Success!")
                .build());
    }

    private PasswordGenSettings updatePasswordGenSettingsEntity(PasswordGenSettingsUpdateRequestDto request, Optional<PasswordGenSettings> passwordGenSettings) {
        if(passwordGenSettings.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password Settings not found for this user");

        PasswordGenSettings foundSettings = passwordGenSettings.get();

        if(request.getPasswordLength() != null)
            foundSettings.setPasswordLength((request.getPasswordLength()));

        if(request.getMinNumOfSpecChars() != null)
            foundSettings.setMinNumOfSpecChars((request.getMinNumOfSpecChars()));

        if(request.getMinNumOfDigits() != null)
            foundSettings.setMinNumOfDigits((request.getMinNumOfDigits()));

        if(request.getHasDigits() != null)
            foundSettings.setHasDigits(request.getHasDigits());

        if(request.getHasLowercase() != null)
            foundSettings.setHasLowercase(request.getHasLowercase());

        if(request.getHasSpecial() != null)
            foundSettings.setHasSpecial(request.getHasSpecial());

        if(request.getHasUppercase() != null)
            foundSettings.setHasUppercase(request.getHasUppercase());

        return foundSettings;
    }

    private int calculatePasswordLength(PasswordGenSettingsUpdateRequestDto request){
        int actualPasswordLength;

        actualPasswordLength =
                Objects.requireNonNullElse(request.getMinNumOfDigits(), 0)
                        + Objects.requireNonNullElse(request.getMinNumOfSpecChars(), 0)
                        + (Boolean.TRUE.equals(request.getHasDigits()) ? 1 : 0)
                        + (Boolean.TRUE.equals(request.getHasLowercase()) ? 1 : 0)
                        + (Boolean.TRUE.equals(request.getHasSpecial()) ? 1 : 0)
                        + (Boolean.TRUE.equals(request.getHasUppercase()) ? 1 : 0);

        return actualPasswordLength;
    }

    public ResponseEntity<PasswordGenSettingsGetResponseDto> updatePasswordGenSettingsEntity(String jwtToken) {
        Long userId = authContextService.extractUserIdFromToken(jwtToken);

        Optional<PasswordGenSettings> foundSettings = passwordGenSettingsRepository.findById(userId);

        if(foundSettings.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password Settings not found for this user");

        PasswordGenSettings passwordSettings = foundSettings.get();

        return ResponseEntity.ok().body(PasswordGenSettingsGetResponseDto.builder()
                        .hasDigits(passwordSettings.getHasDigits())
                        .hasLowercase(passwordSettings.getHasLowercase())
                        .hasSpecial(passwordSettings.getHasSpecial())
                        .hasUppercase(passwordSettings.getHasUppercase())
                        .minNumOfDigits(passwordSettings.getMinNumOfDigits())
                        .minNumOfSpecChars(passwordSettings.getMinNumOfSpecChars())
                        .passwordLength(passwordSettings.getPasswordLength())
                        .build()
        );
    }
}
