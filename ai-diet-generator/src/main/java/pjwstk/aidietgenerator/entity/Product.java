package pjwstk.aidietgenerator.entity;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Product(){
    }

    public Product (String name){
        this.name = name;
    }
}
