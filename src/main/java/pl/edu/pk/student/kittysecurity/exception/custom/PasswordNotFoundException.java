package pl.edu.pk.student.kittysecurity.exception.custom;

public class PasswordNotFoundException extends RuntimeException {
    public PasswordNotFoundException(Long passwordId) {
        super("Password not found with ID: " + passwordId);
    }
}