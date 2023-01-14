package pjwstk.aidietgenerator.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "excluded_products_list_id")
    private ExcludedProductsList excludedProductsList;

    public Product(){
    }

    public Product (String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExcludedProductsList getExcludedProductsList() {
        return excludedProductsList;
    }

    public void setExcludedProductsList(ExcludedProductsList excludedProductsList) {
        this.excludedProductsList = excludedProductsList;
    }
}
