/**
 * CSC 483.1 – Algorithms Analysis and Design
 * Assignment: Algorithm Design, Analysis, and Optimization for Real-World Systems
 *
 * Student : Nwaneri Stephen Osinachi
 * ID      : U2022/5570047
 * Session : 2025/2026 – First Semester
 *
 * Product.java
 * Represents a single item in TechMart's product catalogue.
 * Implements Comparable<Product> so Arrays.sort() can order
 * products by productId without an external Comparator.
 */
public class Product implements Comparable<Product> {

    // ─── Attributes ───────────────────────────────────────────────────────────
    private int    productId;
    private String productName;
    private String category;
    private double price;
    private int    stockQuantity;

    // ─── Constructor ──────────────────────────────────────────────────────────
    /**
     * Constructs a Product with all required attributes.
     *
     * @param productId     unique integer identifier
     * @param productName   human-readable product name
     * @param category      product category (e.g. "Electronics")
     * @param price         retail price in naira (or any currency unit)
     * @param stockQuantity number of units currently in stock
     */
    public Product(int productId, String productName, String category,
                   double price, int stockQuantity) {
        this.productId     = productId;
        this.productName   = productName;
        this.category      = category;
        this.price         = price;
        this.stockQuantity = stockQuantity;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public int    getProductId()     { return productId;     }
    public String getProductName()   { return productName;   }
    public String getCategory()      { return category;      }
    public double getPrice()         { return price;         }
    public int    getStockQuantity() { return stockQuantity; }

    // ─── Setters ──────────────────────────────────────────────────────────────
    public void setProductId(int productId)         { this.productId     = productId;     }
    public void setProductName(String productName)  { this.productName   = productName;   }
    public void setCategory(String category)        { this.category      = category;      }
    public void setPrice(double price)              { this.price         = price;         }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    // ─── Natural ordering ─────────────────────────────────────────────────────
    /**
     * Defines natural ordering by productId (ascending).
     * Used by Arrays.sort() when sorting for binary search.
     *
     * @param other the product to compare against
     * @return negative if this.id < other.id, 0 if equal, positive if greater
     */
    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }

    // ─── Object overrides ─────────────────────────────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        return this.productId == ((Product) obj).productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }

    @Override
    public String toString() {
        return String.format(
            "Product{id=%-6d  name='%-30s'  category='%-12s'  price=%8.2f  stock=%d}",
            productId, productName, category, price, stockQuantity
        );
    }
}
