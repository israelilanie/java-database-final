# Stored Procedures

These stored procedures support analytics workflows. Create them in MySQL and call them as needed.

## Monthly sales by store
```sql
DELIMITER $$
CREATE PROCEDURE GetMonthlySalesForEachStore(IN p_year INT, IN p_month INT)
BEGIN
  SELECT od.store_id AS store_id,
         SUM(od.total_price) AS total_sales,
         p_month AS sale_month,
         p_year AS sale_year
  FROM order_details od
  WHERE YEAR(od.date) = p_year AND MONTH(od.date) = p_month
  GROUP BY od.store_id;
END $$
DELIMITER ;
```

## Aggregate company sales
```sql
DELIMITER $$
CREATE PROCEDURE GetAggregateSalesForCompany(IN p_year INT, IN p_month INT)
BEGIN
  SELECT SUM(od.total_price) AS total_sales,
         p_month AS sale_month,
         p_year AS sale_year
  FROM order_details od
  WHERE YEAR(od.date) = p_year AND MONTH(od.date) = p_month;
END $$
DELIMITER ;
```

## Top-selling products by category
```sql
DELIMITER $$
CREATE PROCEDURE GetTopSellingProductsByCategory(IN p_month INT, IN p_year INT)
BEGIN
  SELECT p.category,
         p.name,
         SUM(oi.quantity) AS total_quantity_sold,
         SUM(oi.quantity * oi.price) AS total_sales
  FROM order_item oi
  JOIN product p ON p.id = oi.product_id
  JOIN order_details od ON od.id = oi.order_id
  WHERE YEAR(od.date) = p_year AND MONTH(od.date) = p_month
  GROUP BY p.category, p.name
  ORDER BY total_sales DESC;
END $$
DELIMITER ;
```
