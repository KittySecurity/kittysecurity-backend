package pl.edu.pk.student.kittysecurity.services;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.exception.custom.UserNotFoundException;
import pl.edu.pk.student.kittysecurity.repository.PasswordGenSettingsRepository;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;
import pl.edu.pk.student.kittysecurity.utils.JwtUtils;

import java.util.Optional;

@Service
public class PasswordGenSettingsService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordGenSettingsRepository passwordGenSettingsRepository;

    public PasswordGenSettingsService(UserRepository userRepo, JwtService jwtService, PasswordGenSettingsRepository passwordGenSettingsRepository) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordGenSettingsRepository = passwordGenSettingsRepository;
    }

    public ResponseEntity<StatusResponseDto> updatePasswordGenSettings(String jwtToken, PasswordGenSettingsUpdateRequestDto request) {
        int calculatedPasswordLength = calculatePasswordLength(request);

        if(calculatedPasswordLength > request.getPasswordLength()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Given configuration exceeds password length!");
        }

        String cleanedToken = JwtUtils.cleanToken(jwtToken);
        Long userId = Long.valueOf(jwtService.extractUserId(cleanedToken));

        User foundUser = findUserById(userId);


        return ResponseEntity.ok().body(StatusResponseDto.builder()
                .status("Password Generation Settings Successfully Updated!")
                .build());
    }

    private int calculatePasswordLength(PasswordGenSettingsUpdateRequestDto request){

        return 10000;
    }

    //TODO: REFACTOR THIS XD DUPLICATE IN USERRSERVICE
    private User findUserById(Long userId){
        Optional<User> foundUser = userRepo.findById(userId);

        if(foundUser.isEmpty()) throw new UserNotFoundException(userId);

        return foundUser.get();
    }
}
