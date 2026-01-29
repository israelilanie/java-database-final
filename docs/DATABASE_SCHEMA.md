# Database Schema

## Relational (MySQL)

### `store`
| Column | Type | Notes |
| --- | --- | --- |
| `id` | BIGINT | PK, auto-increment |
| `name` | VARCHAR | not null |
| `address` | VARCHAR | not null |

### `product`
| Column | Type | Notes |
| --- | --- | --- |
| `id` | BIGINT | PK, auto-increment |
| `name` | VARCHAR | not null |
| `category` | VARCHAR | not null |
| `price` | DOUBLE | not null |
| `sku` | VARCHAR | not null, unique |

### `inventory`
| Column | Type | Notes |
| --- | --- | --- |
| `id` | BIGINT | PK, auto-increment |
| `product_id` | BIGINT | FK → product.id |
| `store_id` | BIGINT | FK → store.id |
| `stock_level` | INT | current stock |

### `customer`
| Column | Type | Notes |
| --- | --- | --- |
| `id` | BIGINT | PK, auto-increment |
| `name` | VARCHAR | not null |
| `email` | VARCHAR | not null |
| `phone` | VARCHAR | not null |

### `order_details`
| Column | Type | Notes |
| --- | --- | --- |
| `id` | BIGINT | PK, auto-increment |
| `customer_id` | BIGINT | FK → customer.id |
| `store_id` | BIGINT | FK → store.id |
| `total_price` | DOUBLE | total order price |
| `date` | DATETIME | order timestamp |

### `order_item`
| Column | Type | Notes |
| --- | --- | --- |
| `id` | BIGINT | PK, auto-increment |
| `order_id` | BIGINT | FK → order_details.id |
| `product_id` | BIGINT | FK → product.id |
| `quantity` | INT | quantity ordered |
| `price` | DOUBLE | item price at time of order |

## NoSQL (MongoDB)

### `reviews`
| Field | Type | Notes |
| --- | --- | --- |
| `_id` | String | MongoDB ObjectId |
| `customerId` | Long | relational customer id |
| `productId` | Long | relational product id |
| `storeId` | Long | relational store id |
| `rating` | Integer | 1-5 rating |
| `comment` | String | freeform text |

## Relationships
- `store` ↔ `inventory`: one-to-many
- `product` ↔ `inventory`: one-to-many
- `customer` ↔ `order_details`: one-to-many
- `order_details` ↔ `order_item`: one-to-many
- `order_item` ↔ `product`: many-to-one
