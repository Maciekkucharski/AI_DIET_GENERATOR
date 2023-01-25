package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.Entity;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Setter
@Getter
public class Post {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "created_at")
    @NotNull
    private Timestamp timestamp;

    @Column(name = "image_path")
    private String imagePath;


    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username"})
    private User user;

    public Post(String title, String description, String imagePath){
        super();
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }


    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
