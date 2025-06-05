package pl.edu.pk.student.kittysecurity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsGetResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.entity.PasswordGenSettings;
import pl.edu.pk.student.kittysecurity.repository.PasswordGenSettingsRepository;
import pl.edu.pk.student.kittysecurity.services.AuthContextService;
import pl.edu.pk.student.kittysecurity.services.PasswordGenSettingsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordGenSettingsServiceTests {

    @Mock
    private AuthContextService authContextService;

    @Mock
    private PasswordGenSettingsRepository passwordGenSettingsRepository;

    @InjectMocks
    private PasswordGenSettingsService service;

    private final String mockToken = "Bearer token";
    private final Long userId = 1L;

    @Test
    void updatePasswordGenSettings_ShouldSaveAndReturnSuccess() {
        PasswordGenSettingsUpdateRequestDto request = PasswordGenSettingsUpdateRequestDto.builder()
                .passwordLength(10)
                .minNumOfDigits(2)
                .minNumOfSpecChars(2)
                .hasDigits(true)
                .hasLowercase(true)
                .hasSpecial(false)
                .hasUppercase(true)
                .build();

        PasswordGenSettings existingSettings = new PasswordGenSettings();
        when(authContextService.extractUserIdFromToken(mockToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.of(existingSettings));
        when(passwordGenSettingsRepository.save(any())).thenReturn(existingSettings);

        ResponseEntity<StatusResponseDto> response = service.updatePasswordGenSettingsEntity(mockToken, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success!", response.getBody().getStatus());

        verify(passwordGenSettingsRepository).save(existingSettings);
    }

    @Test
    void updatePasswordGenSettings_ShouldThrowWhenConfigExceedsLength() {
        PasswordGenSettingsUpdateRequestDto request = PasswordGenSettingsUpdateRequestDto.builder()
                .passwordLength(3)
                .minNumOfDigits(2)
                .minNumOfSpecChars(2)
                .hasDigits(true)
                .hasLowercase(true)
                .hasSpecial(false)
                .hasUppercase(true)
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                service.updatePasswordGenSettingsEntity(mockToken, request));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertEquals("Given configuration exceeds password length!", exception.getReason());
    }

    @Test
    void updatePasswordGenSettings_ShouldThrowWhenSettingsNotFound() {
        PasswordGenSettingsUpdateRequestDto request = PasswordGenSettingsUpdateRequestDto.builder()
                .passwordLength(10)
                .build();

        when(authContextService.extractUserIdFromToken(mockToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                service.updatePasswordGenSettingsEntity(mockToken, request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Password Settings not found for this user", exception.getReason());
    }

    @Test
    void getPasswordGenSettings_ShouldReturnSettings() {
        PasswordGenSettings settings = new PasswordGenSettings();
        settings.setPasswordLength(15);
        settings.setMinNumOfDigits(3);
        settings.setMinNumOfSpecChars(2);
        settings.setHasDigits(true);
        settings.setHasLowercase(false);
        settings.setHasSpecial(true);
        settings.setHasUppercase(false);

        when(authContextService.extractUserIdFromToken(mockToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.of(settings));

        ResponseEntity<PasswordGenSettingsGetResponseDto> response = service.updatePasswordGenSettingsEntity(mockToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        PasswordGenSettingsGetResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals(settings.getPasswordLength(), body.getPasswordLength());
        assertEquals(settings.getMinNumOfDigits(), body.getMinNumOfDigits());
        assertEquals(settings.getMinNumOfSpecChars(), body.getMinNumOfSpecChars());
        assertEquals(settings.getHasDigits(), body.getHasDigits());
        assertEquals(settings.getHasLowercase(), body.getHasLowercase());
        assertEquals(settings.getHasSpecial(), body.getHasSpecial());
        assertEquals(settings.getHasUppercase(), body.getHasUppercase());
    }

    @Test
    void getPasswordGenSettings_ShouldThrowWhenNotFound() {
        when(authContextService.extractUserIdFromToken(mockToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                service.updatePasswordGenSettingsEntity(mockToken));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Password Settings not found for this user", exception.getReason());
    }
}
