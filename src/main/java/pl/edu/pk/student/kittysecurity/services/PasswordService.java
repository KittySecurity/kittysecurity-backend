package pl.edu.pk.student.kittysecurity.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordRequestDto;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordResponseDto;
import pl.edu.pk.student.kittysecurity.entity.PasswordEntry;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.UserNotFoundException;
import pl.edu.pk.student.kittysecurity.repository.PasswordEntryRepository;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;
import pl.edu.pk.student.kittysecurity.utils.JwtUtils;

import java.util.Optional;

@Service
public class PasswordService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEntryRepository passwordEntryRepository;

    public PasswordService(UserRepository userRepo, JwtService jwtService, PasswordEntryRepository passwordEntryRepository) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEntryRepository = passwordEntryRepository;
    }

    public ResponseEntity<CreatePasswordResponseDto> addPasswordByJwt(String jwtToken, CreatePasswordRequestDto request) {
        String cleanedToken = JwtUtils.cleanToken(jwtToken);
        Integer userId = Integer.parseInt(jwtService.extractUserId(cleanedToken));

        User foundUser = findUserById(userId);

        PasswordEntry entry = PasswordEntry.builder()
                .user(foundUser)
                .serviceName(request.getServiceName())
                .url(request.getUrl())
                .login(request.getLogin())
                .passwordEncrypted(request.getPassword())
                .Iv(request.getIv())
                .build();

        passwordEntryRepository.save(entry);

        return ResponseEntity.ok(CreatePasswordResponseDto.builder()
                .status("success")
                .id(entry.getEntryId())
                .serviceName(entry.getServiceName())
                .url(entry.getUrl())
                .login(entry.getLogin())
                .encrypted(entry.getPasswordEncrypted())
                .Iv(entry.getIv())
                .build());
    }

    //TODO: REFACTOR THIS XD DUPLICATE IN USERRSERVICE
    private User findUserById(Integer userId){
        Optional<User> foundUser = userRepo.findById(userId);

        if(foundUser.isEmpty()) throw new UserNotFoundException(userId);

        return foundUser.get();
    }
}
