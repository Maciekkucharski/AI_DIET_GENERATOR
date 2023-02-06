package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties({"credentialsNonExpired", "accountNonExpired",
        "accountNonLocked", "enabled", "password",
        "imagePath", "authorities" , "survey", "rating", "subscribed"})
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    @NotNull
    private String email;

    @Column(name = "login_password")
    @JsonIgnore
    @NotNull
    private String password;

    @Column(name = "authority")
    private String authority;

    @Column(name = "created_at")
    private Timestamp timestamp;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "subscribed")
    private Boolean subscribed;

    @Column(name = "survey")
    private Boolean survey;

    @Column(name = "rating")
    private Boolean rating;

    public User() {

    }

    public User(String email, String password, String authority){
        super();
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public User(String email, String password) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String image_path) {
        this.imagePath = image_path;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
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

    public Boolean getSurvey() {
        return survey;
    }

    public void setSurvey(Boolean survey) {
        this.survey = survey;
    }

    public Boolean getRating() {
        return rating;
    }

    public void setRating(Boolean rating) {
        this.rating = rating;
    }
}
