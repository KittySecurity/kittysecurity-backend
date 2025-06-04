package pl.edu.pk.student.kittysecurity.dto.password.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordSettingsUpdateRequestDto {
    @JsonProperty("length")
    @Size(min = 8, message = "Generated password should be at least 8 characters long")
    @Size(max = 128, message = "Generated password should be not longer than 128 characters")
    private Integer passwordLength;

    @JsonProperty("minNumbers")
    @Size(min = 1, message = "Password should contain at least 1 number")
    @Size(max = 9, message = "Password can contain 9 numbers at maximum")
    private Integer minNumOfDigits;

    @JsonProperty("minSpecials")
    @Size(min = 1, message = "Password should contain at least 1 special character")
    @Size(max = 9, message = "Password can contain 9 special characters at maximum")
    private Integer minNumOfSpecChars;

    @JsonProperty("lowercase")
    private Boolean hasLowercase;

    @JsonProperty("uppercase")
    private Boolean hasUppercase;

    @JsonProperty("specials")
    private Boolean hasSpecial;

    @JsonProperty("numbers")
    private Boolean hasDigits;
}
