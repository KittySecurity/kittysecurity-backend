package pl.edu.pk.student.kittysecurity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String accessTokenType;
    private String accessToken;
    private String refreshToken;
}