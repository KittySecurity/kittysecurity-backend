package pl.edu.pk.student.kittysecurity.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.entity.RefreshToken;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.repository.RefreshTokenRepository;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 3;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepo, UserRepository userRepo) {
        this.refreshTokenRepository = refreshTokenRepo;
        this.userRepository = userRepo;
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        RefreshToken refreshToken = buildToken(user);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = buildToken(user);
        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken buildToken(User user) {
        return RefreshToken.builder()
                .expiresAt(Instant.now().plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS))
                .token(UUID.randomUUID().toString())
                .user(user)
                .build();
    }

    public void findAndDelete(String token) {
        RefreshToken removeToken = findByToken(token);
        refreshTokenRepository.delete(removeToken);
    }

    public RefreshToken rotateRefreshToken(String token) {
        RefreshToken oldRefreshToken = findByToken(token);
        User tokenOwner = oldRefreshToken.getUser();

        verifyExpirationDate(oldRefreshToken);
        refreshTokenRepository.delete(oldRefreshToken);
        return createRefreshToken(tokenOwner);
    }

    private RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("Token doesn't exist!"));
    }

    private void verifyExpirationDate(RefreshToken token) {
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException(token.getToken() + " Refresh token has expired. Login once again");
        }
    }

}
