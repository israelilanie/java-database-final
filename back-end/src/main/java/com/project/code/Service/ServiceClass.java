package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public ServiceClass(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public boolean validateInventory(Inventory inventory) {
        if (inventory == null || inventory.getProduct() == null || inventory.getStore() == null) {
            return false;
        }
        Long productId = inventory.getProduct().getId();
        Long storeId = inventory.getStore().getId();
        if (productId == null || storeId == null) {
            return false;
        }
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId) == null;
    }

    public boolean validateProduct(Product product) {
        if (product == null || product.getName() == null) {
            return false;
        }
        return productRepository.findByNameIgnoreCase(product.getName()) == null;
    }

    public boolean validateProductId(long id) {
        return productRepository.existsById(id);
    }

    public Inventory getInventoryId(Inventory inventory) {
        if (inventory == null || inventory.getProduct() == null || inventory.getStore() == null) {
            return null;
        }
        Long productId = inventory.getProduct().getId();
        Long storeId = inventory.getStore().getId();
        if (productId == null || storeId == null) {
            return null;
        }
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId);
    }
}
