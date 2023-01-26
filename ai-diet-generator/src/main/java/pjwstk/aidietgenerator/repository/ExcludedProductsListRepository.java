package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjwstk.aidietgenerator.entity.ExcludedProductsList;
import pjwstk.aidietgenerator.entity.Product;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

public interface ExcludedProductsListRepository extends JpaRepository<ExcludedProductsList, Long> {

    ExcludedProductsList findByuser(User user);
}
