package pl.edu.pk.student.kittysecurity.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid!")
    private String email;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, message = "Username should be at least 4 characters long")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters and include a digit, a lowercase and an uppercase letter")
    private String password;
}
