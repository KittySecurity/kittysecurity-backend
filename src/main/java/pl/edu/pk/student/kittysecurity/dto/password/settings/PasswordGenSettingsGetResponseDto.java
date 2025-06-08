package pl.edu.pk.student.kittysecurity.dto.password.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
public class PasswordGenSettingsGetResponseDto {

    @JsonProperty("length")
    private Integer passwordLength;

    @JsonProperty("minNumbers")
    private Integer minNumOfDigits;

    @JsonProperty("minSpecial")
    private Integer minNumOfSpecChars;

    @JsonProperty("lowercase")
    private Boolean hasLowercase;

    @JsonProperty("uppercase")
    private Boolean hasUppercase;

    @JsonProperty("special")
    private Boolean hasSpecial;

    @JsonProperty("numbers")
    private Boolean hasDigits;
}
