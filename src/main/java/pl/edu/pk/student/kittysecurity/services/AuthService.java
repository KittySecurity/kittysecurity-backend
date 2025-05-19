package pl.edu.pk.student.kittysecurity.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.dto.JwtResponseDto;
import pl.edu.pk.student.kittysecurity.dto.LoginRequestDto;
import pl.edu.pk.student.kittysecurity.dto.RegisterRequestDto;
import pl.edu.pk.student.kittysecurity.dto.RegisterResponseDto;
import pl.edu.pk.student.kittysecurity.entity.RefreshToken;
import pl.edu.pk.student.kittysecurity.entity.Role;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.UserAlreadyExistsException;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;

import java.util.List;

@Service
public class  AuthService {
    private final String DEFAULT_ROLE = "USER";

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

    public ResponseEntity<RegisterResponseDto> register(RegisterRequestDto registerDto) {
        String newUserEmail = registerDto.getEmail();
        String newUserUsername = registerDto.getUsername();

        checkIfUserExists(newUserUsername, newUserEmail);
        User newUser = createUser(registerDto);
        addDefaultRole(newUser);
        userRepo.save(newUser);

        return ResponseEntity.ok()
                .body(RegisterResponseDto.builder()
                        .email(newUserEmail)
                        .username(newUserUsername)
                        .status("OK")
                        .build());
    }

    private void checkIfUserExists(String username, String email) throws UserAlreadyExistsException {
        if(userRepo.findByUsername(username).isPresent())
            throw new UserAlreadyExistsException("Username is taken!");

        if(userRepo.findByEmail(email).isPresent())
            throw new UserAlreadyExistsException("Email is already in use!");
    }

    private User createUser(RegisterRequestDto registerDto){
        return User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .masterHash(encoder.encode(registerDto.getMasterHash()))
                .build();
    }

    private void addDefaultRole(User user){
        Role userRole = Role.builder()
                .role(DEFAULT_ROLE)
                .user(user)
                .build();

        user.setRoles(List.of(userRole));
    }

    public ResponseEntity<JwtResponseDto> verify(LoginRequestDto request) {
        String email = request.getEmail();
        String masterHash = request.getMasterHash();

        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, masterHash));

        String jwtToken = jwtService.generateToken(email);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);

        return ResponseEntity.ok()
                .body(JwtResponseDto.builder()
                        .accessToken(jwtToken)
                        .accessTokenType("Bearer")
                        .accessTokenExpiresIn(jwtService.extractExpiration(jwtToken).getTime())
                        .refreshToken(refreshToken.getRawToken())
                        .refreshTokenExpiresIn(refreshToken.getExpiresAt().toEpochMilli())
                        .build());
    }

    public ResponseEntity<JwtResponseDto> refreshToken(String token) {
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(token);
        String jwtToken = jwtService.generateToken(newRefreshToken.getUser().getEmail());

        return ResponseEntity.ok()
                .body(JwtResponseDto.builder()
                        .accessToken(jwtToken)
                        .accessTokenType("Bearer")
                        .accessTokenExpiresIn(jwtService.extractExpiration(jwtToken).getTime())
                        .refreshToken(newRefreshToken.getRawToken())
                        .refreshTokenExpiresIn(newRefreshToken.getExpiresAt().toEpochMilli())
                        .build());
    }

    public void revokeToken(String token) {
        refreshTokenService.findAndDelete(token);
    }

    //TODO: REMOVE
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
