package pl.edu.pk.student.kittysecurity.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pk.student.kittysecurity.services.PasswordSettingsService;

@RestController
@RequestMapping("api/v1/settings")
public class PasswordSettingsController {

    private PasswordSettingsService settingsService;

    public PasswordSettingsController(PasswordSettingsService settingsService){
        this.settingsService = settingsService;
    }

    @PutMapping
    public void updateUserPasswordGenSettings(){

    }
}
