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
import pl.edu.pk.student.kittysecurity.services.JwtService;
import pl.edu.pk.student.kittysecurity.services.PasswordGenSettingsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordGenSettingsServiceTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordGenSettingsRepository passwordGenSettingsRepository;

    @InjectMocks
    private PasswordGenSettingsService service;

    private final String mockToken = "Bearer token";
    private final String cleanedToken = "token";
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
        existingSettings.setPasswordLength(8);
        existingSettings.setMinNumOfDigits(1);
        existingSettings.setMinNumOfSpecChars(1);
        existingSettings.setHasDigits(false);
        existingSettings.setHasLowercase(false);
        existingSettings.setHasSpecial(false);
        existingSettings.setHasUppercase(false);

        when(jwtService.extractUserId(cleanedToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.of(existingSettings));
        PasswordGenSettings mockSaved = new PasswordGenSettings();
        when(passwordGenSettingsRepository.save(any())).thenReturn(mockSaved);

        try (MockedStatic<pl.edu.pk.student.kittysecurity.utils.JwtUtils> mockedJwtUtils = Mockito.mockStatic(pl.edu.pk.student.kittysecurity.utils.JwtUtils.class)) {
            mockedJwtUtils.when(() -> pl.edu.pk.student.kittysecurity.utils.JwtUtils.cleanToken(mockToken)).thenReturn(cleanedToken);

            ResponseEntity<StatusResponseDto> response = service.updatePasswordGenSettingsEntity(mockToken, request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Success!", response.getBody().getStatus());

            ArgumentCaptor<PasswordGenSettings> captor = ArgumentCaptor.forClass(PasswordGenSettings.class);
            verify(passwordGenSettingsRepository).save(captor.capture());

            PasswordGenSettings savedSettings = captor.getValue();
            assertEquals(request.getPasswordLength(), savedSettings.getPasswordLength());
            assertEquals(request.getMinNumOfDigits(), savedSettings.getMinNumOfDigits());
            assertEquals(request.getMinNumOfSpecChars(), savedSettings.getMinNumOfSpecChars());
            assertEquals(request.getHasDigits(), savedSettings.getHasDigits());
            assertEquals(request.getHasLowercase(), savedSettings.getHasLowercase());
            assertEquals(request.getHasSpecial(), savedSettings.getHasSpecial());
            assertEquals(request.getHasUppercase(), savedSettings.getHasUppercase());
        }
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

        try (MockedStatic<pl.edu.pk.student.kittysecurity.utils.JwtUtils> mockedJwtUtils = Mockito.mockStatic(pl.edu.pk.student.kittysecurity.utils.JwtUtils.class)) {
            mockedJwtUtils.when(() -> pl.edu.pk.student.kittysecurity.utils.JwtUtils.cleanToken(mockToken)).thenReturn(cleanedToken);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    service.updatePasswordGenSettingsEntity(mockToken, request));

            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
            assertEquals("Given configuration exceeds password length!", exception.getReason());
        }
    }

    @Test
    void updatePasswordGenSettings_ShouldThrowWhenSettingsNotFound() {
        PasswordGenSettingsUpdateRequestDto request = PasswordGenSettingsUpdateRequestDto.builder()
                .passwordLength(10)
                .build();

        when(jwtService.extractUserId(cleanedToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.empty());

        try (MockedStatic<pl.edu.pk.student.kittysecurity.utils.JwtUtils> mockedJwtUtils = Mockito.mockStatic(pl.edu.pk.student.kittysecurity.utils.JwtUtils.class)) {
            mockedJwtUtils.when(() -> pl.edu.pk.student.kittysecurity.utils.JwtUtils.cleanToken(mockToken)).thenReturn(cleanedToken);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    service.updatePasswordGenSettingsEntity(mockToken, request));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("Password Settings not found for this user", exception.getReason());
        }
    }

    @Test
    void updatePasswordGenSettings_ShouldReturnSettings() {
        PasswordGenSettings settings = new PasswordGenSettings();
        settings.setPasswordLength(15);
        settings.setMinNumOfDigits(3);
        settings.setMinNumOfSpecChars(2);
        settings.setHasDigits(true);
        settings.setHasLowercase(false);
        settings.setHasSpecial(true);
        settings.setHasUppercase(false);

        when(jwtService.extractUserId(cleanedToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.of(settings));

        try (MockedStatic<pl.edu.pk.student.kittysecurity.utils.JwtUtils> mockedJwtUtils = Mockito.mockStatic(pl.edu.pk.student.kittysecurity.utils.JwtUtils.class)) {
            mockedJwtUtils.when(() -> pl.edu.pk.student.kittysecurity.utils.JwtUtils.cleanToken(mockToken)).thenReturn(cleanedToken);

            ResponseEntity<PasswordGenSettingsGetResponseDto> response = service.updatePasswordGenSettingsEntity(mockToken);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(settings.getPasswordLength(), response.getBody().getPasswordLength());
            assertEquals(settings.getMinNumOfDigits(), response.getBody().getMinNumOfDigits());
            assertEquals(settings.getMinNumOfSpecChars(), response.getBody().getMinNumOfSpecChars());
            assertEquals(settings.getHasDigits(), response.getBody().getHasDigits());
            assertEquals(settings.getHasLowercase(), response.getBody().getHasLowercase());
            assertEquals(settings.getHasSpecial(), response.getBody().getHasSpecial());
            assertEquals(settings.getHasUppercase(), response.getBody().getHasUppercase());
        }
    }

    @Test
    void updatePasswordGenSettings_ShouldThrowWhenNotFound() {
        when(jwtService.extractUserId(cleanedToken)).thenReturn(userId);
        when(passwordGenSettingsRepository.findById(userId)).thenReturn(Optional.empty());

        try (MockedStatic<pl.edu.pk.student.kittysecurity.utils.JwtUtils> mockedJwtUtils = Mockito.mockStatic(pl.edu.pk.student.kittysecurity.utils.JwtUtils.class)) {
            mockedJwtUtils.when(() -> pl.edu.pk.student.kittysecurity.utils.JwtUtils.cleanToken(mockToken)).thenReturn(cleanedToken);

            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                    service.updatePasswordGenSettingsEntity(mockToken));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("Password Settings not found for this user", exception.getReason());
        }
    }
}
