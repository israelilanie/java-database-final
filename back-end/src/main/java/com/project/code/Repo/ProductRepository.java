package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    Product findBySku(String sku);

    Product findByName(String name);

    Product findByNameIgnoreCase(String name);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category);

    @Query("select p from Product p join p.inventory i where i.store.id = :storeId")
    List<Product> findByStoreId(@Param("storeId") Long storeId);
}
