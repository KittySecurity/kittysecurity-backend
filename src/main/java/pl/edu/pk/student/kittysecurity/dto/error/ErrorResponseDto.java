package pl.edu.pk.student.kittysecurity.dto.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}