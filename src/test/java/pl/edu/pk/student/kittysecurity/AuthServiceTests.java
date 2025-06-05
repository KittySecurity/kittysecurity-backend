package pl.edu.pk.student.kittysecurity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import pl.edu.pk.student.kittysecurity.dto.auth.JwtResponseDto;
import pl.edu.pk.student.kittysecurity.dto.auth.LoginRequestDto;
import pl.edu.pk.student.kittysecurity.entity.RefreshToken;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;
import pl.edu.pk.student.kittysecurity.services.AuthService;
import pl.edu.pk.student.kittysecurity.services.JwtService;
import pl.edu.pk.student.kittysecurity.services.RefreshTokenService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private AuthService authService;

    @Test
    public void loginUserSuccess() {
        Long userId = 500L;
        String username = "testUser";
        String password = "testPass";
        String jwtToken = "mocked-jwt-token";
        String refreshToken = "mocked-refresh-token";

        LoginRequestDto request = new LoginRequestDto(username, password);

        // Mocks
        Authentication auth = mock(Authentication.class);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        User mockUser = new User();
        mockUser.setUserId(userId);
        when(userRepo.findByEmail(username)).thenReturn(Optional.of(mockUser));

        when(jwtService.generateToken(userId)).thenReturn(jwtToken);
        when(jwtService.extractExpiration(jwtToken)).thenReturn(new Date(1234567890L));

        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setRawToken(refreshToken);
        mockRefreshToken.setExpiresAt(Instant.ofEpochMilli(1234567999L));
        mockRefreshToken.setUser(mockUser);

        when(refreshTokenService.createRefreshToken(username)).thenReturn(mockRefreshToken);

        // Act
        ResponseEntity<JwtResponseDto> response = authService.verify(request);

        // Assert
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        JwtResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Bearer", body.getAccessTokenType());
        assertEquals(jwtToken, body.getAccessToken());
        assertEquals(refreshToken, body.getRefreshToken());
        assertEquals(1234567890L, body.getAccessTokenExpiresIn());
        assertEquals(1234567999L, body.getRefreshTokenExpiresIn());
    }

    @Test
    public void shouldThrowUserFailExceptionWhenCredentialsIncorrect() {
        String email = "wrongUser";
        String password = "wrongPassword";

        LoginRequestDto request = new LoginRequestDto(email, password);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.verify(request));
    }
}
