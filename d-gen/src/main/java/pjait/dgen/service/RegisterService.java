package pjait.dgen.service;

public class RegisterService {
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String password;
    private final String username;

    public RegisterService(String first_name, String last_name, String email, String username, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public String getFirst_name() { return first_name; }

    public String getLast_name() { return last_name; }

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
