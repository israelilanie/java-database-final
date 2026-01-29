package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ServiceClass serviceClass;

    public InventoryController(ProductRepository productRepository,
                               InventoryRepository inventoryRepository,
                               ServiceClass serviceClass) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.serviceClass = serviceClass;
    }

    @PutMapping
    public Map<String, Object> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, Object> response = new HashMap<>();
        if (request == null || request.getProduct() == null || request.getInventory() == null) {
            response.put("message", "Invalid request payload");
            return response;
        }

        Product product = request.getProduct();
        Inventory inventory = request.getInventory();

        if (!serviceClass.validateProductId(product.getId())) {
            response.put("message", "No product found for id " + product.getId());
            return response;
        }

        productRepository.save(product);

        Inventory existingInventory = serviceClass.getInventoryId(inventory);
        if (existingInventory == null) {
            response.put("message", "No inventory data available for this product and store");
            return response;
        }

        existingInventory.setStockLevel(inventory.getStockLevel());
        inventoryRepository.save(existingInventory);
        response.put("message", "Inventory updated successfully");
        return response;
    }

    @PostMapping
    public Map<String, Object> saveInventory(@RequestBody Inventory inventory) {
        Map<String, Object> response = new HashMap<>();
        if (!serviceClass.validateInventory(inventory)) {
            response.put("message", "Inventory already exists");
            return response;
        }
        inventoryRepository.save(inventory);
        response.put("message", "Inventory added successfully");
        return response;
    }

    @GetMapping("/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId) {
        List<Inventory> inventories = inventoryRepository.findByStore_Id(storeId);
        List<Product> products = inventories.stream()
                .map(inventory -> {
                    Product product = inventory.getProduct();
                    product.setInventory(List.of(inventory));
                    return product;
                })
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        return response;
    }

    @GetMapping("/filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(@PathVariable String category,
                                              @PathVariable String name,
                                              @PathVariable Long storeId) {
        List<Inventory> inventories = inventoryRepository.findByStore_Id(storeId);
        List<Product> products = inventories.stream()
                .map(Inventory::getProduct)
                .filter(product -> {
                    boolean categoryMatches = "null".equalsIgnoreCase(category)
                            || (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category));
                    boolean nameMatches = "null".equalsIgnoreCase(name)
                            || (product.getName() != null && product.getName().toLowerCase().contains(name.toLowerCase()));
                    return categoryMatches && nameMatches;
                })
                .map(product -> {
                    Inventory matchingInventory = inventories.stream()
                            .filter(inv -> inv.getProduct().getId().equals(product.getId()))
                            .findFirst()
                            .orElse(null);
                    if (matchingInventory != null) {
                        product.setInventory(List.of(matchingInventory));
                    }
                    return product;
                })
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("product", products);
        return response;
    }

    @GetMapping("/search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable Long storeId) {
        List<Inventory> inventories = inventoryRepository.findByStore_Id(storeId);
        List<Product> products = inventories.stream()
                .map(Inventory::getProduct)
                .filter(product -> product.getName() != null
                        && product.getName().toLowerCase().contains(name.toLowerCase()))
                .map(product -> {
                    Inventory matchingInventory = inventories.stream()
                            .filter(inv -> inv.getProduct().getId().equals(product.getId()))
                            .findFirst()
                            .orElse(null);
                    if (matchingInventory != null) {
                        product.setInventory(List.of(matchingInventory));
                    }
                    return product;
                })
                .collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("product", products);
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> removeProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (!serviceClass.validateProductId(id)) {
            response.put("message", "No product found with id " + id);
            return response;
        }
        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        response.put("message", "Product removed successfully");
        return response;
    }

    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable Integer quantity,
                                    @PathVariable Long storeId,
                                    @PathVariable Long productId) {
        Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
        if (inventory == null || inventory.getStockLevel() == null) {
            return false;
        }
        return inventory.getStockLevel() >= quantity;
    }
}
