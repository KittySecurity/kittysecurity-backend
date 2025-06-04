package pl.edu.pk.student.kittysecurity.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.settings.PasswordGenSettingsUpdateRequestDto;
import pl.edu.pk.student.kittysecurity.services.PasswordGenSettingsService;

import java.time.Instant;

@RestController
@RequestMapping("api/v1/settings")
public class PasswordGenSettingsController {

    private PasswordGenSettingsService settingsService;

    public PasswordGenSettingsController(PasswordGenSettingsService settingsService){
        this.settingsService = settingsService;
    }

    @PutMapping
    public ResponseEntity<StatusResponseDto> updatePasswordGenSettings(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, PasswordGenSettingsUpdateRequestDto request){
        return settingsService.updatePasswordGenSettings(jwtToken, request);
    }
}
