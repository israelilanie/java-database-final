# REST API Overview

Base URL: `http://localhost:8080`

## Store
- `POST /store` - Create store
- `GET /store/validate/{storeId}` - Validate store ID
- `POST /store/placeOrder` - Place an order

## Product
- `POST /product` - Create product
- `GET /product` - List products
- `GET /product/product/{id}` - Get product by ID
- `PUT /product` - Update product
- `DELETE /product/{id}` - Delete product
- `GET /product/searchProduct/{name}` - Search product by name
- `GET /product/category/{name}/{category}` - Filter by name/category
- `GET /product/filter/{category}/{storeid}` - Filter products by store + category

## Inventory
- `POST /inventory` - Add product to inventory
- `PUT /inventory` - Update product + inventory
- `GET /inventory/{storeId}` - List products for a store
- `GET /inventory/filter/{category}/{name}/{storeId}` - Filter inventory by category/name
- `GET /inventory/search/{name}/{storeId}` - Search inventory by name
- `DELETE /inventory/{id}` - Remove product
- `GET /inventory/validate/{quantity}/{storeId}/{productId}` - Validate stock

## Reviews
- `GET /reviews/{storeId}/{productId}` - Get reviews for product in store

## Errors
- `400 Bad Request` if JSON payload is malformed.
