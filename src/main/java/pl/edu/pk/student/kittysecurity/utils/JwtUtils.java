package pl.edu.pk.student.kittysecurity.utils;

public class JwtUtils {

    public static String cleanToken(String token) {
        if (token == null) {
            return null;
        }
        token = token.trim();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        return token;
    }
}