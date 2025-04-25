package pl.edu.pk.student.kittysecurity.dto;

public class LoginResponseDTO {
    private String message;
    private String access_token;

    public LoginResponseDTO(String message, String token) {
        this.message = message;
        this.access_token = token;
    }

    public LoginResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
