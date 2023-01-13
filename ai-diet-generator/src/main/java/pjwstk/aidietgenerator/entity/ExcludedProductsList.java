package pjwstk.aidietgenerator.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "excluded_products")
public class ExcludedProductsList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<Product> excludedProducts;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
