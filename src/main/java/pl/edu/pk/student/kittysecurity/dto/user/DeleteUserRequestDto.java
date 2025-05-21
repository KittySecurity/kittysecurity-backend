package pl.edu.pk.student.kittysecurity.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequestDto {

    @NotBlank
    @JsonProperty("master_hash")
    private String masterHash;
}
