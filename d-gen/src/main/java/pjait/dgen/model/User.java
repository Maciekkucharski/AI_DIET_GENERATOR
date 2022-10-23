package pjait.dgen.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "login_password")
    private String password;

    @Column(name = "authority")
    private String authority;

    public User() {

    }

    public User(String username, String password, String authority){
        super();
        this.username = username;
        this.password = password;
        this.authority = authority;
    }

    public User(String firstName, String lastName, String email, String username, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(this.authority.split(","))
                .map(String::trim)
                .filter(authority -> !authority.equals(""))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void addAuthority(GrantedAuthority authority) {
        String newAuthority = authority.getAuthority().trim();
        String currentAuthorities = this.authority == null ? "" : (this.authority + ",");
        this.authority = currentAuthorities + newAuthority;
    }

    public void removeAuthority(GrantedAuthority authority) {
        String deletedAuthority = authority.getAuthority().trim();
        String remainingAuthorities = this.authority.replace(deletedAuthority, "")
                .replace(",,", "")
                .trim();
        this.authority = remainingAuthorities;
    }
}