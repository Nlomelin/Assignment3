public class Product {
    private String productId;
    private String name;
    private String category;
    private String price; // Change type to String to accommodate "Invalid Price"

    public Product(String productId, String name, String category, String price) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters for the product fields
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId + ", Name: " + name + ", Category: " + category + ", Price: " + price;
    }
}
