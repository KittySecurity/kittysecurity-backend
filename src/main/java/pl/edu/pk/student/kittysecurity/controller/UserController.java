package pl.edu.pk.student.kittysecurity.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pk.student.kittysecurity.dto.auth.LoginRequestDto;
import pl.edu.pk.student.kittysecurity.dto.other.StatusResponseDto;
import pl.edu.pk.student.kittysecurity.dto.user.DeleteUserRequestDto;
import pl.edu.pk.student.kittysecurity.dto.user.UserResponseDto;
import pl.edu.pk.student.kittysecurity.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserResponseDto> getUserData(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken){
        return userService.getUserDataByJwt(jwtToken);
    }

    @DeleteMapping("")
    public ResponseEntity<StatusResponseDto> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken, @RequestBody @Valid DeleteUserRequestDto request){
        return userService.deleteUserByJwt(jwtToken, request);
    }
}
