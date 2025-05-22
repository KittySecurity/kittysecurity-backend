package pl.edu.pk.student.kittysecurity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordEntry {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private UUID entryId;

    @Column(nullable = false)
    private UUID userId;

    private String serviceName;

    private String url;

    private String login;

    private String passwordEncrypted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
