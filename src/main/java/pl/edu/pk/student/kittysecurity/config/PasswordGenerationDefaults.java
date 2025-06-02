package pl.edu.pk.student.kittysecurity.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "password-generation.default")
@Getter
@Setter
public class PasswordGenerationDefaults {
    private Integer length;
    private Integer minNumOfSpecChars;
    private Integer minNumOfDigits;
    private boolean includeUppercase;
    private boolean includeLowercase;
    private boolean includeDigits;
    private boolean includeSpecials;
}
