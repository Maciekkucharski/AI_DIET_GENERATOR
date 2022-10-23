package pjait.dgen.service;

public class LoginService {
    private final String username;
    private final String password;

    public LoginService (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }
}
