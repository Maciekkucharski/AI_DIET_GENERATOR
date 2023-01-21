package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.DietWeek;
import pjwstk.aidietgenerator.entity.ExcludedProductsList;
import pjwstk.aidietgenerator.entity.Product;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.ExcludedProductsListRepository;
import pjwstk.aidietgenerator.repository.ProductRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.WeekDietRepository;
import pjwstk.aidietgenerator.request.DietRequest;
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
    private final ExcludedProductsListRepository excludedProductsListRepository;

    private final WeekDietRepository weekDietRepository;

    @Autowired
    public DietController(DietService dietService, RecipeRepository recipeRepository, UserDetailsService userDetailsService,
                          ProductRepository productRepository, ExcludedProductsListRepository excludedProductsListRepository,
                          WeekDietRepository weekDietRepository){
        this.dietService = dietService;
        this.recipeRepository = recipeRepository;
        this.userDetailsService = userDetailsService;
        this.productRepository = productRepository;
        this.excludedProductsListRepository = excludedProductsListRepository;
        this.weekDietRepository = weekDietRepository;
    }

    @PostMapping("/generate")
    @Transactional
    public DietWeek getIds(@RequestBody DietRequest dietRequest, HttpServletResponse response) throws IOException {
        return dietService.generateDiet(dietRequest, response);
    }

    @GetMapping()
    public DietWeek getCurrentUserDiet(){
        return weekDietRepository.findByuser(userDetailsService.findCurrentUser());
    }

    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/excludedProducts")
    public ExcludedProductsList getCurrentUserExcludedProductsList(){
        return excludedProductsListRepository.findByuser(userDetailsService.findCurrentUser());
    }

    @PostMapping("/excludedProducts")
    public ExcludedProductsList setExcludedProductsList(@RequestBody List<Long> ids, HttpServletResponse response){
        return dietService.setExcludedProducts(response, ids);
    }
}
