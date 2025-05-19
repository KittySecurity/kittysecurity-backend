package pl.edu.pk.student.kittysecurity.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    private String status;
    private String email;
    private String username;
}