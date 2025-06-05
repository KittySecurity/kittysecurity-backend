package pl.edu.pk.student.kittysecurity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import pl.edu.pk.student.kittysecurity.controller.PasswordGenSettingsController;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsGetResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.services.PasswordGenSettingsService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PasswordGenSettingsControllerTests {

    @Mock
    private PasswordGenSettingsService settingsService;

    @InjectMocks
    private PasswordGenSettingsController controller;

    private final String mockJwtToken = "Bearer mocked.jwt.token";

    @Test
    public void getPasswordGenSettings_ShouldReturnSettings() {
        PasswordGenSettingsGetResponseDto mockResponseDto = PasswordGenSettingsGetResponseDto.builder()
                .passwordLength(12)
                .minNumOfDigits(2)
                .minNumOfSpecChars(1)
                .hasLowercase(true)
                .hasUppercase(true)
                .hasSpecial(true)
                .hasDigits(true)
                .build();

        ResponseEntity<PasswordGenSettingsGetResponseDto> mockResponse =
                ResponseEntity.ok(mockResponseDto);

        Mockito.when(settingsService.updatePasswordGenSettingsEntity(mockJwtToken)).thenReturn(mockResponse);

        ResponseEntity<PasswordGenSettingsGetResponseDto> response = controller.getPasswordGenSettings(mockJwtToken);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockResponseDto.getPasswordLength(), Objects.requireNonNull(response.getBody()).getPasswordLength());
        assertEquals(mockResponseDto.getMinNumOfDigits(), response.getBody().getMinNumOfDigits());
        assertEquals(mockResponseDto.getMinNumOfSpecChars(), response.getBody().getMinNumOfSpecChars());
        assertEquals(mockResponseDto.getHasLowercase(), response.getBody().getHasLowercase());
        assertEquals(mockResponseDto.getHasUppercase(), response.getBody().getHasUppercase());
        assertEquals(mockResponseDto.getHasSpecial(), response.getBody().getHasSpecial());
        assertEquals(mockResponseDto.getHasDigits(), response.getBody().getHasDigits());
    }

    @Test
    public void updatePasswordGenSettings_ShouldReturnSuccessStatus() {
        PasswordGenSettingsUpdateRequestDto mockRequest = PasswordGenSettingsUpdateRequestDto.builder()
                .passwordLength(12)
                .minNumOfDigits(2)
                .minNumOfSpecChars(1)
                .hasLowercase(true)
                .hasUppercase(true)
                .hasSpecial(true)
                .hasDigits(true)
                .build();

        StatusResponseDto mockStatus = StatusResponseDto.builder()
                .status("Success!")
                .build();

        ResponseEntity<StatusResponseDto> mockResponse = ResponseEntity.ok(mockStatus);

        Mockito.when(settingsService.updatePasswordGenSettingsEntity(mockJwtToken, mockRequest)).thenReturn(mockResponse);

        ResponseEntity<StatusResponseDto> response = controller.updatePasswordGenSettings(mockJwtToken, mockRequest);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Success!", Objects.requireNonNull(response.getBody()).getStatus());
    }
}
