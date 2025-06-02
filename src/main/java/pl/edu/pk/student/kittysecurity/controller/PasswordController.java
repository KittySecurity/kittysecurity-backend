package pl.edu.pk.student.kittysecurity.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordRequestDto;
import pl.edu.pk.student.kittysecurity.dto.password.CreatePasswordResponseDto;
import pl.edu.pk.student.kittysecurity.dto.password.PasswordEntryDto;
import pl.edu.pk.student.kittysecurity.services.PasswordService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PasswordController {

    PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("password")
    public ResponseEntity<CreatePasswordResponseDto> addPassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @Valid @RequestBody CreatePasswordRequestDto request){
        return passwordService.addPasswordByJwt(jwtToken, request);
    }

    @GetMapping("passwords")
    public ResponseEntity<List<PasswordEntryDto>> getAllPasswords(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken){
        return passwordService.getAllPasswordsByJwt(jwtToken);
    }
  
    @GetMapping("password/{passwordId}")
    public ResponseEntity<PasswordEntryDto> getPassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @PathVariable Long passwordId){
        return passwordService.getPasswordByIdAndJwt(jwtToken, passwordId);
    }

    @DeleteMapping("password/{passwordId}")
    public ResponseEntity<StatusResponseDto> removePassword(@PathVariable Long passwordId){
        return passwordService.removePasswordByIdAndJwt(passwordId);
    }
}
