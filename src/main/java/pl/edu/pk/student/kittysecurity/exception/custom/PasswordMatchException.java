package pl.edu.pk.student.kittysecurity.exception.custom;

public class PasswordMatchException extends RuntimeException {
    public PasswordMatchException(String message){
        super(message);
    }
}
