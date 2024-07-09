package ru.clevertec.check;

import java.util.Objects;

public class DiscountCard {
    private Long id;
    private Long number;
    private Double discountAmount;

    public DiscountCard() {
    }

    public DiscountCard(Double discountAmount, Long number) {
        this.discountAmount = discountAmount;
        this.number = number;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard that = (DiscountCard) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(discountAmount, that.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, discountAmount);
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "id=" + id +
                ", number=" + number +
                ", discountAmount=" + discountAmount +
                '}';
    }
}
