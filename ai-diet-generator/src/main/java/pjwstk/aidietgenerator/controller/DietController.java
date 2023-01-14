package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Product;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.ExcludedProductListRepository;
import pjwstk.aidietgenerator.repository.ProductRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.request.RecipeRequest;
import pjwstk.aidietgenerator.service.DietService;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/diet")
public class DietController {

    private final DietService dietService;
    private final RecipeRepository recipeRepository;
    private final UserDetailsService userDetailsService;
    private final ProductRepository productRepository;
    private final ExcludedProductListRepository excludedProductListRepository;

    @Autowired
    public DietController(DietService dietService, RecipeRepository recipeRepository, UserDetailsService userDetailsService,
                          ProductRepository productRepository, ExcludedProductListRepository excludedProductListRepository){
        this.dietService = dietService;
        this.recipeRepository = recipeRepository;
        this.userDetailsService = userDetailsService;
        this.productRepository = productRepository;
        this.excludedProductListRepository = excludedProductListRepository;
    }

    @PostMapping("/generate")
    @Transactional
    public void getIds() throws IOException {
        User currentUser = userDetailsService.findCurrentUser();
        dietService.getRecommendedIds(currentUser.getId());
    }

    @GetMapping("/excludeProducts")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @PostMapping("/excludeProducts")
    public void setExcludedProducts(@RequestBody RecipeRequest recipeRequest, HttpServletResponse response){
    }
}
