package pl.edu.pk.student.kittysecurity.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {

    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @NotBlank(message = "MasterHash cannot be blank!")
    @JsonProperty("master_hash")
    private String masterHash;
}
