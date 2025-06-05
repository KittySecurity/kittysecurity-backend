package pl.edu.pk.student.kittysecurity.services;

import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.UserNotFoundException;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;
import pl.edu.pk.student.kittysecurity.utils.JwtUtils;

@Service
public class AuthContextService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthContextService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public Long extractUserIdFromToken(String rawToken) {
        String cleaned = JwtUtils.cleanToken(rawToken);
        return jwtService.extractUserId(cleaned);
    }

    public User getUserFromToken(String rawToken) {
        Long userId = extractUserIdFromToken(rawToken);
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
