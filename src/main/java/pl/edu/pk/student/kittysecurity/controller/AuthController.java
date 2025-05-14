package pl.edu.pk.student.kittysecurity.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.student.kittysecurity.dto.JwtResponseDto;
import pl.edu.pk.student.kittysecurity.dto.LoginRequestDto;
import pl.edu.pk.student.kittysecurity.dto.RefreshTokenRequestDto;
import pl.edu.pk.student.kittysecurity.dto.RegisterRequestDto;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.services.AuthService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody @Valid RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto request) {
        return authService.verify(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> revokeToken(@RequestBody RefreshTokenRequestDto request) {
        authService.revokeToken(request.getToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        return authService.refreshToken(request.getToken());
    }

    //TODO: REMOVE
    @GetMapping("/users")
    public List<User> getUsers() {
        return authService.getAllUsers();
    }
}
