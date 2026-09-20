package vn.iotstar.controller.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import vn.iotstar.dto.CategoryPage;
import vn.iotstar.dto.ProductPage;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class GraphQLApiController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @QueryMapping
    public List<Product> productsPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return List.of();
        return productRepository.findByCategory(category);
    }

    // Trả về ProductPage DTO chuẩn
    @QueryMapping
    public ProductPage productsPaged(@Argument String keyword, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending());
        Page<Product> pPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            pPage = productRepository.findByProductNameContainingIgnoreCase(keyword.trim(), pageable);
        } else {
            pPage = productRepository.findAll(pageable);
        }
        return new ProductPage(pPage.getContent(), pPage.getTotalPages(), (int) pPage.getTotalElements(), page);
    }

    // Trả về CategoryPage DTO chuẩn
    @QueryMapping
    public CategoryPage categoriesPaged(@Argument String keyword, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("categoryId").descending());
        Page<Category> cPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            cPage = categoryRepository.findByCategorynameContainingIgnoreCase(keyword.trim(), pageable);
        } else {
            cPage = categoryRepository.findAll(pageable);
        }
        return new CategoryPage(cPage.getContent(), cPage.getTotalPages(), (int) cPage.getTotalElements(), page);
    }

    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public Category categoryById(@Argument Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Product productById(@Argument Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Category createCategory(@Argument Map<String, Object> input) {
        Category c = new Category();
        c.setCategoryname((String) input.get("categoryname"));
        c.setImages((String) input.get("images"));
        c.setStatus(input.get("status") != null ? (Integer) input.get("status") : 1);
        return categoryRepository.save(c);
    }

    @MutationMapping
    public Category updateCategory(@Argument Integer id, @Argument Map<String, Object> input) {
        Category c = categoryRepository.findById(id).orElseThrow();
        c.setCategoryname((String) input.get("categoryname"));
        if (input.get("images") != null) c.setImages((String) input.get("images"));
        if (input.get("status") != null) c.setStatus((Integer) input.get("status"));
        return categoryRepository.save(c);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Integer id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @MutationMapping
    public Product createProduct(@Argument Map<String, Object> input) {
        Product p = new Product();
        p.setProductName((String) input.get("productName"));
        p.setPrice(((Number) input.get("price")).doubleValue());
        p.setImageUrl((String) input.get("imageUrl"));
        p.setDescription((String) input.get("description"));
        p.setCreatedAt(new Date());

        Integer catId = Integer.valueOf(input.get("categoryId").toString());
        p.setCategory(categoryRepository.findById(catId).orElse(null));
        return productRepository.save(p);
    }

    @MutationMapping
    public Product updateProduct(@Argument Integer id, @Argument Map<String, Object> input) {
        Product p = productRepository.findById(id).orElseThrow();
        p.setProductName((String) input.get("productName"));
        p.setPrice(((Number) input.get("price")).doubleValue());
        if (input.get("imageUrl") != null) p.setImageUrl((String) input.get("imageUrl"));
        p.setDescription((String) input.get("description"));

        Integer catId = Integer.valueOf(input.get("categoryId").toString());
        p.setCategory(categoryRepository.findById(catId).orElse(null));
        return productRepository.save(p);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Integer id) {
        productRepository.deleteById(id);
        return true;
    }
}