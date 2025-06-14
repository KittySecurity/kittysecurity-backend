package pl.edu.pk.student.kittysecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordRequestDto;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.PasswordEntryDto;
import pl.edu.pk.student.kittysecurity.entity.PasswordEntry;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.PasswordNotFoundException;
import pl.edu.pk.student.kittysecurity.repository.PasswordEntryRepository;
import pl.edu.pk.student.kittysecurity.services.AuthContextService;
import pl.edu.pk.student.kittysecurity.services.PasswordService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordServiceTests {

    @Mock
    private PasswordEntryRepository passwordEntryRepository;

    @Mock
    private AuthContextService authContext;

    @InjectMocks
    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String JWT_TOKEN = "Bearer token";
    private static final Long USER_ID = 42L;

    private User sampleUser() {
        User user = new User();
        user.setUserId(USER_ID);
        return user;
    }

    private PasswordEntry sampleEntry(User user) {
        return PasswordEntry.builder()
                .entryId(1L)
                .user(user)
                .serviceName("service")
                .url("http://example.com")
                .login("login")
                .passwordEncrypted("encryptedPass")
                .Iv("iv")
                .build();
    }

    @Test
    void addPasswordByJwt_shouldAddAndReturnResponse() {
        User user = sampleUser();
        CreatePasswordRequestDto request = CreatePasswordRequestDto.builder()
                .serviceName("service")
                .url("http://example.com")
                .login("login")
                .password("encryptedPass")
                .Iv("iv")
                .build();

        when(authContext.getUserFromToken(JWT_TOKEN)).thenReturn(user);
        when(passwordEntryRepository.save(any(PasswordEntry.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<CreatePasswordResponseDto> response = passwordService.addPasswordByJwt(JWT_TOKEN, request);

        assertEquals("success", Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("service", response.getBody().getServiceName());
        assertEquals("http://example.com", response.getBody().getUrl());
        assertEquals("login", response.getBody().getLogin());
        assertEquals("encryptedPass", response.getBody().getEncrypted());
        assertEquals("iv", response.getBody().getIv());
    }

    @Test
    void getAllPasswordsByJwt_shouldReturnList() {
        User user = sampleUser();
        PasswordEntry entry = sampleEntry(user);

        when(authContext.getUserFromToken(JWT_TOKEN)).thenReturn(user);
        when(passwordEntryRepository.findByUser(user)).thenReturn(List.of(entry));

        ResponseEntity<List<PasswordEntryDto>> response = passwordService.getAllPasswordsByJwt(JWT_TOKEN);

        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        PasswordEntryDto dto = response.getBody().getFirst();
        assertEquals(entry.getEntryId(), dto.getEntryId());
        assertEquals(entry.getServiceName(), dto.getServiceName());
        assertEquals(entry.getUrl(), dto.getUrl());
        assertEquals(entry.getLogin(), dto.getLogin());
        assertEquals(entry.getPasswordEncrypted(), dto.getPasswordEncrypted());
        assertEquals(entry.getIv(), dto.getIv());
    }

    @Test
    void getPasswordByIdAndJwt_shouldReturnPasswordEntry() {
        User user = sampleUser();
        PasswordEntry entry = sampleEntry(user);

        when(authContext.getUserFromToken(JWT_TOKEN)).thenReturn(user);
        when(passwordEntryRepository.findByUserAndEntryId(user, entry.getEntryId())).thenReturn(Optional.of(entry));

        ResponseEntity<PasswordEntryDto> response = passwordService.getPasswordByIdAndJwt(JWT_TOKEN, entry.getEntryId());

        assertEquals(entry.getEntryId(), Objects.requireNonNull(response.getBody()).getEntryId());
        assertEquals(entry.getServiceName(), response.getBody().getServiceName());
    }

    @Test
    void getPasswordByIdAndJwt_passwordNotFound_shouldThrow() {
        User user = sampleUser();

        when(authContext.getUserFromToken(JWT_TOKEN)).thenReturn(user);
        when(passwordEntryRepository.findByUserAndEntryId(user, 999L)).thenReturn(Optional.empty());

        assertThrows(PasswordNotFoundException.class, () -> passwordService.getPasswordByIdAndJwt(JWT_TOKEN, 999L));
    }
}
