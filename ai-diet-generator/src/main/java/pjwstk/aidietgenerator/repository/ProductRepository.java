package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
