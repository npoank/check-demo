package ru.clevertec.check;

import java.util.Objects;

public class Product {
    private Long id;
    private String description;
    private Double price;
    private Long quantityInStock;
    private boolean wholesaleProduct;

    public Product(Long id, String description, Double price, Long quantityInStock, boolean wholesaleProduct) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.wholesaleProduct = wholesaleProduct;
    }

    public Long getQuantityInStock() {
        return quantityInStock;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public void setQuantityInStock(Long quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public boolean isWholesaleProduct() {
        return wholesaleProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return wholesaleProduct == product.wholesaleProduct && Objects.equals(id, product.id) && Objects.equals(description, product.description) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, wholesaleProduct);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", wholesaleProduct=" + wholesaleProduct +
                '}';
    }
}
