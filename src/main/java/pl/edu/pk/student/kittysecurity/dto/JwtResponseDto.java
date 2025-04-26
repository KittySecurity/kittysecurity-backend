package pl.edu.pk.student.kittysecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String message;
    private String access_token_type;
    private String access_token;
    private String refresh_token;
}