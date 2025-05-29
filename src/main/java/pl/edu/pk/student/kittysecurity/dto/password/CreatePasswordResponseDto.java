package pl.edu.pk.student.kittysecurity.dto.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePasswordResponseDto {
    private String status;

    private Long id;

    private String serviceName;

    private String url;

    private String login;

    private String encrypted;

    @JsonProperty("IV")
    private String Iv;
}
