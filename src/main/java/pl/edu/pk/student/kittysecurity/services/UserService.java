package pl.edu.pk.student.kittysecurity.services;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.user.DeleteUserRequestDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserResponseDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserUpdateResponseDto;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.PasswordMatchException;
import pl.edu.pk.student.kittysecurity.exception.custom.UserNotFoundException;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;
import pl.edu.pk.student.kittysecurity.utils.JwtUtils;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepo, JwtService jwtService, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    public ResponseEntity<UserResponseDto> getUserDataByJwt(String jwtToken) {

        User foundUser = findUserById(jwtService.extractUserId(JwtUtils.cleanToken(jwtToken)));

        return ResponseEntity.ok().body(UserResponseDto.builder()
                .email(foundUser.getEmail())
                .id(foundUser.getUserId())
                .username(foundUser.getDisplayName())
                .createdAt(foundUser.getCreatedAt().toEpochMilli())
                .updatedAt(foundUser.getUpdatedAt().toEpochMilli())
                .build()
        );
    }

    private User findUserById(Long userId){
        Optional<User> foundUser = userRepo.findById(userId);

        if(foundUser.isEmpty()) throw new UserNotFoundException(userId);

        return foundUser.get();
    }

    public ResponseEntity<UserUpdateResponseDto> updateUserByJwt(String jwtToken, @Valid UserUpdateRequestDto request) {
        User foundUser = findUserById(jwtService.extractUserId(JwtUtils.cleanToken(jwtToken)));

        if(!StringUtils.isBlank(request.getDisplayName()))
            foundUser.setDisplayName(request.getDisplayName());

        if(!StringUtils.isBlank(request.getEmail()))
            foundUser.setEmail(request.getEmail());

        if(!StringUtils.isBlank(request.getMasterHash()))
            foundUser.setMasterHash(encoder.encode(request.getMasterHash()));

        User updatedUser = userRepo.save(foundUser);

        return ResponseEntity.ok(UserUpdateResponseDto.builder()
                .status("OK")
                .displayName(updatedUser.getDisplayName())
                .email(updatedUser.getEmail())
                .updatedAt(updatedUser.getUpdatedAt().toEpochMilli())
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponseDto> deleteUserByJwt(String jwtToken, DeleteUserRequestDto request) {
        User foundUser = findUserById(jwtService.extractUserId(JwtUtils.cleanToken(jwtToken)));

        if(encoder.matches(request.getMasterHash(), foundUser.getMasterHash())) userRepo.delete(foundUser);
        else throw new PasswordMatchException("Passwords do not match!");

        return ResponseEntity.ok().body(StatusResponseDto.builder()
                .status("OK")
                .build());
    }
}
