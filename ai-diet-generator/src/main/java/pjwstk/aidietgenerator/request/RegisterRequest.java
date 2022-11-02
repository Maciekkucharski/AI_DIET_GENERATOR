package pjwstk.aidietgenerator.request;


public class RegisterRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String username;

    public RegisterRequest(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public String getFirst_name() { return firstName; }

    public String getLast_name() { return lastName; }

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
