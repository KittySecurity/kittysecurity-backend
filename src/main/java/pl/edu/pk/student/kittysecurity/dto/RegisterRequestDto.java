package pl.edu.pk.student.kittysecurity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @NotBlank(message = "MasterHash cannot be blank")
    @JsonProperty("master_hash")
    private String masterHash;

}
