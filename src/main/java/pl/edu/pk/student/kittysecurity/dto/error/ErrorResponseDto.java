package pl.edu.pk.student.kittysecurity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponseDto {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}