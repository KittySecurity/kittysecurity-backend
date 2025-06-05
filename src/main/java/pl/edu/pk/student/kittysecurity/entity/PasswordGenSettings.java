package pl.edu.pk.student.kittysecurity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "password_gen_settings")
public class PasswordGenSettings {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "length")
    private Integer passwordLength;

    private Integer minNumOfSpecChars;

    private Integer minNumOfDigits;

    private Boolean hasLowercase;

    private Boolean hasUppercase;

    private Boolean hasSpecial;

    private Boolean hasDigits;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
