package com.project.code.Service;

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public OrderService(CustomerRepository customerRepository,
                        StoreRepository storeRepository,
                        OrderDetailsRepository orderDetailsRepository,
                        OrderItemRepository orderItemRepository,
                        InventoryRepository inventoryRepository,
                        ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
        if (placeOrderRequest == null) {
            throw new IllegalArgumentException("Order request cannot be null");
        }

        Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());
        if (customer == null) {
            customer = new Customer(
                    placeOrderRequest.getCustomerName(),
                    placeOrderRequest.getCustomerEmail(),
                    placeOrderRequest.getCustomerPhone()
            );
            customer = customerRepository.save(customer);
        }

        Store store = storeRepository.findById(placeOrderRequest.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        OrderDetails orderDetails = new OrderDetails(
                customer,
                store,
                placeOrderRequest.getTotalPrice(),
                LocalDateTime.now()
        );
        orderDetails = orderDetailsRepository.save(orderDetails);

        if (placeOrderRequest.getPurchaseProduct() == null) {
            return;
        }

        for (PurchaseProductDTO purchase : placeOrderRequest.getPurchaseProduct()) {
            Product product = productRepository.findById(purchase.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(product.getId(), store.getId());
            if (inventory == null) {
                throw new IllegalArgumentException("Inventory not found for product");
            }

            int requestedQty = purchase.getQuantity();
            int currentStock = inventory.getStockLevel() == null ? 0 : inventory.getStockLevel();
            if (currentStock < requestedQty) {
                throw new IllegalArgumentException("Insufficient stock for product " + product.getName());
            }

            inventory.setStockLevel(currentStock - requestedQty);
            inventoryRepository.save(inventory);

            OrderItem orderItem = new OrderItem(orderDetails, product, requestedQty, purchase.getPrice());
            orderItemRepository.save(orderItem);
        }
    }
}
