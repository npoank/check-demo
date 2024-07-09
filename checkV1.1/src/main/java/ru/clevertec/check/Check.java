package ru.clevertec.check;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Check {

    private String date;
    private String time;
    private Double totalPrice;
    private Double totalDiscount;
    private Double totalWithDiscount;

    private DiscountCard discountCard;
    private List<CheckRow> checkRows = new ArrayList<>();

    public Check(DiscountCard discountCard) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.date = LocalDate.now().format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.time = LocalTime.now().format(timeFormatter);

        // если есть дисконтная карта из списка карт, то скидка равна скидке по карте
        // если нет, то скидка 2%
        if (discountCard != null) {
            this.discountCard = discountCard;
        } else {
            this.discountCard = new DiscountCard(2.0, new Long(CheckRunner.discountCardIdFromConsole));
        }
    }

    public Check() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.date = LocalDate.now().format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.time = LocalTime.now().format(timeFormatter);
    }

    public void addToCheck(CartItem cartItem) {
        // на основании продуктов в корзине и их колличестве, создаем строчку в чеке
        Map<Product, Integer> productsInCart = cartItem.getProductsInCart();
        for (Map.Entry<Product, Integer> p : productsInCart.entrySet()) {
            checkRows.add(new CheckRow(p.getKey(), p.getValue(), discountCard));
        }

        // считаем TOTAL PRICE, TOTAL DISCOUNT, TOTAL WITH DISCOUNT
        double sumOfTotal = 0;
        double sumDiscount = 0;
        for (CheckRow checkRow : checkRows) {
            sumOfTotal = sumOfTotal + checkRow.getTotalCoast();
            sumDiscount = sumDiscount + checkRow.getDiscount();
        }
        this.totalPrice = sumOfTotal;
        this.totalDiscount = sumDiscount;
        this.totalWithDiscount = totalPrice - totalDiscount;
        if (this.totalWithDiscount > Double.parseDouble(CheckRunner.balanceFromConsole)) {
            throw new RuntimeException("BAD REQUEST");
        }
    }

    public static class CheckRow {
        private Integer qty;
        private String productDescription;
        private Double price;
        private Double totalCoast;
        private Double discount;
        private Integer minQuantityForWholesale = 5;

        public CheckRow(Product product, Integer quantity, DiscountCard discountCard) {
            this.qty = quantity;
            this.productDescription = product.getDescription();
            this.price = product.getPrice();
            this.totalCoast = product.getPrice() * quantity;

            if (product.isWholesaleProduct() && quantity >= minQuantityForWholesale) {
                double temp = Math.round(totalCoast * 10);
                this.discount = temp / 100;
            } else if (discountCard != null) {
                double temp = (totalCoast * (discountCard.getDiscountAmount() / 100)) * 100;
                temp = Math.round(temp);
                this.discount = temp / 100;
            } else {
                this.discount = 0.00;
            }
        }

        @Override
        public String toString() {
            return "CheckRow{" +
                    "qty=" + qty +
                    ", productDescription='" + productDescription + '\'' +
                    ", price=" + price +
                    ", totalCoast=" + totalCoast +
                    ", discount=" + discount +
                    '}';
        }

        public Integer getQty() {
            return qty;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public Double getPrice() {
            return price;
        }

        public Double getTotalCoast() {
            return totalCoast;
        }

        public Double getDiscount() {
            return discount;
        }
    }

    @Override
    public String toString() {
        return "Check{" +
                "checkRows=" + checkRows +
                '}';
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public Double getTotalWithDiscount() {
        return totalWithDiscount;
    }

    public List<CheckRow> getCheckRows() {
        return checkRows;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }
}
