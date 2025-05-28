package pl.edu.pk.student.kittysecurity.dto.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePasswordRequestDto {

    private String url;

    private String login;

    @JsonProperty("name")
    private String serviceName;

    @JsonProperty("encrypted")
    private String password;

    @JsonProperty("IV")
    private String Iv;
}
