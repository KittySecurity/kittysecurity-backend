package pl.edu.pk.student.kittysecurity.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.dto.JwtResponseDto;
import pl.edu.pk.student.kittysecurity.entity.RefreshToken;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepo, AuthenticationManager authManager,
                       BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwt,
                       RefreshTokenService refreshTokenService) {
        this.userRepo = userRepo;
        this.encoder = bCryptPasswordEncoder;
        this.authManager = authManager;
        this.jwtService = jwt;
        this.refreshTokenService = refreshTokenService;
    }

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    //TODO: HANDLE EXCEPTION AND REMOVE SECOND RETURN
    public ResponseEntity<JwtResponseDto> verify(User user) {
        Authentication authentication = authManager.
                authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok()
                    .body(new JwtResponseDto("Login successful", "Bearer",
                            jwtService.generateToken(user.getUsername()), refreshTokenService.createRefreshToken(user.getUsername()).getToken()));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new JwtResponseDto("Login Unsuccessful", "", "", ""));
    }

    public ResponseEntity<JwtResponseDto> refreshToken(String token) {
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(token);

        return ResponseEntity.ok()
                .body(new JwtResponseDto("Login successful", "Bearer",
                        jwtService.generateToken(newRefreshToken.getUser().getUsername()), newRefreshToken.getToken()));
    }

    public void revokeToken(String token) {
        refreshTokenService.findAndDelete(token);
    }

    //TODO: REMOVE
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
