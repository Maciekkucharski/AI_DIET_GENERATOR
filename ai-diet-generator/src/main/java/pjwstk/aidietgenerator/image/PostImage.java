package pjwstk.aidietgenerator.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.NoArgsConstructor;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.User;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PostImage {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "image_path")
    private String imagePath;

    @OneToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username"})
    private Post post;

}
