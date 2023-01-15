package pjwstk.aidietgenerator.imageGCS.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.Post;
import javax.persistence.*;

@Entity
@Table(name = "post_image")
@NoArgsConstructor
@Setter
@Getter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path")
    private String imagePath;

    @OneToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;
}
