package pl.edu.pk.student.kittysecurity.dto.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePasswordRequestDto {

    @NotNull
    private String url;

    @NotNull
    private String login;

    @NotNull
    @JsonProperty("name")
    private String serviceName;

    @NotNull
    @JsonProperty("encrypted")
    private String password;

    @NotNull
    @JsonProperty("IV")
    private String Iv;
}
