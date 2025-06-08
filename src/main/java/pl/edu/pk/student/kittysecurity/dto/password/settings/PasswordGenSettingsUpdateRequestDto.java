package pl.edu.pk.student.kittysecurity.dto.password.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordGenSettingsUpdateRequestDto {

    @Min(value = 8, message = "Generated password should be at least 8 characters long")
    @Max(value = 128, message = "Generated password should be not longer than 128 characters")
    @JsonProperty("length")
    private Integer passwordLength;


    @Min(value = 1, message = "Password should contain at least 1 number")
    @Max(value = 9, message = "Password can contain 9 numbers at maximum")
    @JsonProperty("minNumbers")
    private Integer minNumOfDigits;


    @Min(value = 1, message = "Password should contain at least 1 special character")
    @Max(value = 9, message = "Password can contain 9 special characters at maximum")
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
