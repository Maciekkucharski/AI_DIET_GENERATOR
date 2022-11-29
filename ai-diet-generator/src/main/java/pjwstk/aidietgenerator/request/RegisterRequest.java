package pjwstk.aidietgenerator.request;


public class RegisterRequest {

    private final String email;
    private final String password;

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
