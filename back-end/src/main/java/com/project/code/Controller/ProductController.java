package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ServiceClass serviceClass;
    private final InventoryRepository inventoryRepository;

    public ProductController(ProductRepository productRepository,
                             ServiceClass serviceClass,
                             InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.serviceClass = serviceClass;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping
    public Map<String, Object> addProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        if (!serviceClass.validateProduct(product)) {
            response.put("message", "Product already exists");
            return response;
        }

        try {
            productRepository.save(product);
            response.put("message", "Product added successfully");
        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Product SKU must be unique");
        }
        return response;
    }

    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        productRepository.findById(id).ifPresent(product -> response.put("products", product));
        return response;
    }

    @PutMapping
    public Map<String, Object> updateProduct(@RequestBody Product product) {
        productRepository.save(product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product updated successfully");
        return response;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable String name,
                                                       @PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products;
        if ("null".equalsIgnoreCase(name)) {
            products = productRepository.findByCategoryIgnoreCase(category);
        } else if ("null".equalsIgnoreCase(category)) {
            products = productRepository.findByNameContainingIgnoreCase(name);
        } else {
            products = productRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(name, category);
        }
        response.put("products", products);
        return response;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());
        return response;
    }

    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable String category,
                                                              @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findByStoreId(storeid).stream()
                .filter(product -> product.getCategory() != null
                        && product.getCategory().equalsIgnoreCase(category))
                .toList();
        response.put("product", products);
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        if (!serviceClass.validateProductId(id)) {
            response.put("message", "No product found with id " + id);
            return response;
        }
        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        response.put("message", "Product deleted successfully");
        return response;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findByNameContainingIgnoreCase(name));
        return response;
    }
}
