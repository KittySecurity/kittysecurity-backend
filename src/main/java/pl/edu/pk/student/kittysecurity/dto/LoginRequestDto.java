package pl.edu.pk.student.kittysecurity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    @NotBlank(message = "Username cannot be blank!")
    private String username;
    @NotBlank(message = "Password cannot be blank!")
    private String password;
}
