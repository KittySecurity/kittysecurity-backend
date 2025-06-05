package pl.edu.pk.student.kittysecurity.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsGetResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.services.PasswordGenSettingsService;

@RestController
@RequestMapping("api/v1/settings")
public class PasswordGenSettingsController {

    private final PasswordGenSettingsService settingsService;

    public PasswordGenSettingsController(PasswordGenSettingsService settingsService){
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<PasswordGenSettingsGetResponseDto> getPasswordGenSettings(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken){
        return settingsService.updatePasswordGenSettingsEntity(jwtToken);
    }

    @PutMapping
    public ResponseEntity<StatusResponseDto> updatePasswordGenSettings(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @RequestBody  @Valid PasswordGenSettingsUpdateRequestDto request){
        return settingsService.updatePasswordGenSettingsEntity(jwtToken, request);
    }
}
