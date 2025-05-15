package pl.edu.pk.student.kittysecurity.exception.custom;


public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
