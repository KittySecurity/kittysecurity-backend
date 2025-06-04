package pl.edu.pk.student.kittysecurity.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pk.student.kittysecurity.entity.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTests {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("73378465b637475fb8c7dd100429dcaf5b4dcc2cee4e4535dfc2cdf4d839bd501b81e4cb8738a4066cfcb7523b5f94e818080fd4e6fd5671c04554f2ef27a2000accbe76113b14a14a03077ea351923a46003d8d7dd098034a61e8fdbd054780eaafbed4d53f6267b1ca1abb750006bd2e036ca6f31456739410d51d0545935f2b184808cdc36fb4e1b18521013189eec9a0f9d2fd1325e56006bf48ecd12ec7b2d81c19f91aae9feb949603f7c40f39f8affe3d3d156c082eaccb82c674cb7e22c98f0d31e05cb9e21da6fbb5afdcd87a23b6a67605d5863dc60bb7a412674ee12ff5388c6756bbac834a5c514bda805a98d040f7293f99012c0428407b3223",
                90000000);
    }

    @Test
    void shouldGenerateToken() {
        Long userId = 123L;

        String token = jwtService.generateToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUserIdFromToken() {
        Long userId = 456L;
        String token = jwtService.generateToken(userId);

        String extractedUserId = jwtService.extractUserId(token);

        assertEquals(String.valueOf(userId), extractedUserId);
    }

    @Test
    void shouldValidateValidToken() {
        Long userId = 789L;
        String token = jwtService.generateToken(userId);

        User user = new User();
        user.setUserId(userId);

        boolean isValid = jwtService.validateToken(token, user);

        assertTrue(isValid);
    }

    @Test
    void shouldDetectTokenExpirationCorrectly() {
        Long userId = 1L;
        String token = jwtService.generateToken(userId);

        Date expirationDate = jwtService.extractExpiration(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }
}
