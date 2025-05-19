package pl.edu.pk.student.kittysecurity.exception.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("User not found with ID: " + userId);
    }
}