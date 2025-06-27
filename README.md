# EasyShop E-Commerce API – Backend Capstone

This is the backend API for **EasyShop**, an online store where customers can browse products, manage a shopping cart, and place orders. This capstone builds on an existing version by fixing bugs, adding new features, and demonstrating integration with a front-end client.

## Project Goals

- Debug issues from Version 1 of the codebase
- Add new REST API features (categories, cart, checkout)
- Protect admin-only routes
- Write and add new logic
- Use Postman for endpoint testing
- Run the front-end client to verify the API in action

<details>

<summary>Tech Stack</summary>

- **Backend:** Java, Spring Boot
- **Database:** MySQL
- **Testing:** Postman
- **Tools:** IntelliJ IDEA, GitHub, Maven
</details>

## Notable Feature

**Improved Product Search & Update**

One key fix was repairing the product search and update logic. Products now filter correctly by category, price, and color, and updates modify the product instead of creating duplicates.

```java
public List<Product> searchProducts(int catId, BigDecimal minPrice, BigDecimal maxPrice, String color) {
    String sql = "SELECT * FROM products WHERE category_id = ? AND price BETWEEN ? AND ? AND color = ?";
    // Execute query safely with parameters
}
