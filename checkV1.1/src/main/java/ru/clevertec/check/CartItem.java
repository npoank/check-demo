package ru.clevertec.check;

import java.util.HashMap;
import java.util.Map;

public class CartItem {
    private Map<Product, Integer> productsInCart = new HashMap<>();

    public CartItem() {
    }

    public Map<Product, Integer> getProductsInCart() {
        return productsInCart;
    }

    public void addToCart(Product product, Integer quantity) {
        // если хотим добавить товаров в корзину больше чем их есть в наличии
        if (product.getQuantityInStock() < quantity) {
            throw new RuntimeException("Product: " + product.getDescription() + " out of stock");
        } else {
            productsInCart.put(product, quantity);
            product.setQuantityInStock(product.getQuantityInStock() - quantity);
        }

//        // если такая позиция товара есть в корзине, то просто увеличиваем кол-во в корзине
//        // и уменьшаем кол-во товара в наличии
//        if (productsInCart.containsKey(product)) {
//            productsInCart.put(product, productsInCart.get(product) + quantity);
//            product.setQuantityInStock(product.getQuantityInStock() - quantity);
//        } else {
//            productsInCart.put(product, quantity);
//            product.setQuantityInStock(product.getQuantityInStock() - quantity);
//        }

    }
}
