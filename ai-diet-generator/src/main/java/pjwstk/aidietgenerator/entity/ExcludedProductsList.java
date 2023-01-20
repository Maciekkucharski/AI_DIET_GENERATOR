package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "excluded_products")
public class ExcludedProductsList {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Product> listOfExcludedProducts;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public ExcludedProductsList(){
    }

    public ExcludedProductsList(List<Product> listOfExcludedProducts, User user) {
        this.listOfExcludedProducts = listOfExcludedProducts;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getListOfExcludedProducts() {
        return listOfExcludedProducts;
    }

    public void setListOfExcludedProducts(List<Product> listOfExcludedProducts) {
        this.listOfExcludedProducts = listOfExcludedProducts;
    }
}
