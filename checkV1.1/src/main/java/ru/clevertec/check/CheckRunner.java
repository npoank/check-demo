package ru.clevertec.check;

import java.io.*;
import java.util.*;

public class CheckRunner {
    private static final String PRODUCTS_FILE = "./src/main/resources/products.csv";
    private static final String DISCOUNT_CARDS_FILE = "./src/main/resources/discountCards.csv";
    public static String RESULT_FILE = "./src/result.csv";
    public static String discountCardIdFromConsole = null;
    public static String balanceFromConsole = null;

    public static void main(String[] args) {

        // java -cp target/classes ru.clevertec.check.CheckRunner 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100

        // java -cp target/classes ru.clevertec.check.CheckRunner 1-1 discountCard=1111 balanceDebitCard=100
// 1.
        // считываем товары из products.csv
        Map<String, Product> allProducts = readProducts(PRODUCTS_FILE);
        //showProducts(allProducts);
        System.out.println("-----------------------------------------------------------------------");

// 2.
        // считываем дисконтные карты из discountCards.csv
        Map<String, DiscountCard> allDiscountCards = readCards(DISCOUNT_CARDS_FILE);
        //showCards(allDiscountCards);
        System.out.println("-----------------------------------------------------------------------");

// 3.
        // считываем параметры из консоли
        Map<Product, Integer> productsFromConsole = new HashMap<>();
        String productDelimiter = "-";
        String cardAndBalanceDelimiter = "=";
        if (args.length < 1) {
            throw new RuntimeException("Enter min 1 arguments");
        }
        for (String arg : args) {
            // считываение с консоли товаров и их кол-ва
            if (arg.contains("-")) {
                String[] sudStr = arg.split(productDelimiter);
                if (allProducts.get(sudStr[0]) == null) {
                    throw new RuntimeException("Product with this id not found");
                }
                // группировка повторяющихся по id товаров и подсчет их общего кол-ва (1-2 1-3 = 1-4)
                Product consoleProduct = allProducts.get(sudStr[0]);
                Integer consoleProductQuantity = Integer.valueOf(sudStr[sudStr.length - 1]);
                if (productsFromConsole.containsKey(consoleProduct)){
                    productsFromConsole.put(consoleProduct, productsFromConsole.get(consoleProduct) + consoleProductQuantity);
                } else {
                    productsFromConsole.put(consoleProduct, consoleProductQuantity);
                }
            // считываение с консоли номера дисконтной карты
            } else if (arg.contains("discountCard")) {
                String[] sudStr = arg.split(cardAndBalanceDelimiter);
                discountCardIdFromConsole = sudStr[sudStr.length - 1];
            // считываение с консоли баланса средств
            } else if (arg.contains("balanceDebitCard")) {
                String[] sudStr = arg.split(cardAndBalanceDelimiter);
                balanceFromConsole = sudStr[sudStr.length - 1];
            }
        }
        System.out.println("-----------------------------------------------------------------------");

// 4.
        // добавляем товары в корзину
        // также здесь посчитаем сколько товаров осталось в наличии
        CartItem cartItem = new CartItem();
        System.out.println(productsFromConsole);
        for (Map.Entry<Product, Integer> p : productsFromConsole.entrySet()) {
            System.out.println(p);
            cartItem.addToCart(p.getKey(), p.getValue());
        }
        System.out.println("-----------------------------------------------------------------------");

// 5.
        // создание чека и калькуляция цен и скидок
        Check check = new Check();
        if (discountCardIdFromConsole != null) {
            check = new Check(allDiscountCards.get(discountCardIdFromConsole));
        }
        if (balanceFromConsole == null) {
            throw new RuntimeException("BAD REQUEST");
        }
        check.addToCheck(cartItem);

        // печать чека в консоль
        System.out.println("Date: " + check.getDate());
        System.out.println("Time: " + check.getTime());

        if (check.getDiscountCard() != null) {
            System.out.println("Discount card: " + check.getDiscountCard().getNumber());
            System.out.println("Discount Percentage: " + check.getDiscountCard().getDiscountAmount());
        }

        for (int i = 0; i < check.getCheckRows().size(); i++) {
            System.out.println(check.getCheckRows().get(i));
        }
        System.out.println("Total price: " + check.getTotalPrice());
        System.out.println("Total discount: " + check.getTotalDiscount());
        System.out.println("Total with discount: " + check.getTotalWithDiscount());

// 6.
        // вывод чека в csv-файл
        Printer printer = new Printer();
        printer.print(check);
    }

    // метод для преобразования строк csv-файла в объекты класса DiscountCard
    private static Map<String, DiscountCard> readCards(String filename) {
        Map<String, DiscountCard> discountCards = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // пропустить первую строку с именами столбцов
            while ((line = br.readLine()) != null) {
                DiscountCard discountCard = new DiscountCard();
                String[] values = line.split(";");
                discountCard.setId(Long.valueOf(values[0].trim()));
                discountCard.setNumber(Long.valueOf(values[1].trim()));
                discountCard.setDiscountAmount(Double.valueOf(values[2].trim()));
                discountCards.put(values[1].trim(), discountCard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return discountCards;
    }

    // метод для преобразования строк csv-файла в объекты класса Product
    private static Map<String, Product> readProducts(String filename) {
        Map<String, Product> products = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();// to skip first row with columns names
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                String id = values[0].trim();
                String description = values[1].trim();
                String strPrice = values[2].trim();
                Double price = Double.parseDouble(strPrice.replace(",", "."));
                long qtyStock = Integer.parseInt(values[3].trim());
                boolean wholesaleProduct = values[4].equals("+");
                products.put(id, new Product(Long.parseLong(id), description, price, qtyStock, wholesaleProduct));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}