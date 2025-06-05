package pl.edu.pk.student.kittysecurity.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordRequestDto;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.PasswordEntryDto;
import pl.edu.pk.student.kittysecurity.entity.PasswordEntry;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.PasswordNotFoundException;
import pl.edu.pk.student.kittysecurity.exception.custom.UserNotFoundException;
import pl.edu.pk.student.kittysecurity.repository.PasswordEntryRepository;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;
import pl.edu.pk.student.kittysecurity.utils.JwtUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public ResponseEntity<CreatePasswordResponseDto> addPasswordByJwt(String jwtToken, CreatePasswordRequestDto request) {
        String cleanedToken = JwtUtils.cleanToken(jwtToken);
        Long userId = jwtService.extractUserId(cleanedToken);

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
                .build()
        );
    }

    //TODO: REFACTOR THIS XD DUPLICATE IN USERRSERVICE
    private User findUserById(Long userId){
        Optional<User> foundUser = userRepo.findById(userId);

        if(foundUser.isEmpty()) throw new UserNotFoundException(userId);

        return foundUser.get();
    }

    public ResponseEntity<List<PasswordEntryDto>> getAllPasswordsByJwt(String jwtToken) {
        String cleanedToken = JwtUtils.cleanToken(jwtToken);
        Long userId = jwtService.extractUserId(cleanedToken);

        User foundUser = findUserById(userId);

        List<PasswordEntry> entries = passwordEntryRepository.findByUser(foundUser);

        return ResponseEntity.ok().body(entries.stream().map(
                entry -> PasswordEntryDto.builder()
                        .entryId(entry.getEntryId())
                        .serviceName(entry.getServiceName())
                        .url(entry.getUrl())
                        .login(entry.getLogin())
                        .passwordEncrypted(entry.getPasswordEncrypted())
                        .Iv(entry.getIv())
                        .build()
        ).collect(Collectors.toList()));
    }

    @Transactional
    public ResponseEntity<StatusResponseDto> removePasswordByIdAndJwt(Long passwordId){

        Optional<PasswordEntry> foundEntry = passwordEntryRepository.findByEntryId(passwordId);

        if(foundEntry.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password entry not found.");

        passwordEntryRepository.delete(foundEntry.get());

        return ResponseEntity.ok().body(StatusResponseDto.builder()
                            .status("Success")
                            .build()
        );
    }

    public ResponseEntity<PasswordEntryDto> getPasswordByIdAndJwt(String jwtToken, Long entryId) {
        String cleanedToken = JwtUtils.cleanToken(jwtToken);
        Long userId = jwtService.extractUserId(cleanedToken);

        User foundUser = findUserById(userId);

        Optional<PasswordEntry> entry = passwordEntryRepository.findByUserAndEntryId(foundUser, entryId);

        if(entry.isEmpty()) throw new PasswordNotFoundException(entryId);

        PasswordEntry foundEntry = entry.get();

        return ResponseEntity.ok().body(PasswordEntryDto.builder()
                        .entryId(foundEntry.getEntryId())
                        .serviceName(foundEntry.getServiceName())
                        .url(foundEntry.getUrl())
                        .login(foundEntry.getLogin())
                        .passwordEncrypted(foundEntry.getPasswordEncrypted())
                        .Iv(foundEntry.getIv())
                        .build()
        );
    }
}
