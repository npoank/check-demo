package ru.clevertec.check;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Printer {
    private Check check;

    public void print(Check check) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Date", "Time"});
        data.add(new String[]{check.getDate(), check.getTime()});
        data.add(new String[]{" "});

        data.add(new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"});
        List<Check.CheckRow> checkRows = check.getCheckRows();
        for (Check.CheckRow rows : checkRows) {
            data.add(new String[]{
                    String.valueOf(rows.getQty()),
                    rows.getProductDescription(),
                    String.format("%.2f", rows.getPrice()) + "$",
                    String.format("%.2f", rows.getDiscount()) + "$",
                    String.format("%.2f", rows.getTotalCoast()) + "$"
            });
        }
        data.add(new String[]{" "});

        if (check.getDiscountCard() != null) {
            data.add(new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"});
            data.add(new String[]{
                    String.valueOf(check.getDiscountCard().getNumber()),
                    check.getDiscountCard().getDiscountAmount() + "%"});
        }
        data.add(new String[]{" "});

        data.add(new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"});
        data.add(new String[]{
                String.format("%.2f", check.getTotalPrice()) + "$",
                String.format("%.2f", check.getTotalDiscount()) + "$",
                String.format("%.2f", check.getTotalWithDiscount()) + "$"});

        // Запись данных в CSV файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CheckRunner.RESULT_FILE))) {
            for (String[] rowData : data) {
                writer.write(String.join(";", rowData));
                writer.newLine();
            }
            System.out.println("Data successfully written to file " + CheckRunner.RESULT_FILE);
        } catch (IOException e) {
            System.err.println("Error writing data to file " + CheckRunner.RESULT_FILE + ": " + e.getMessage());
        }
    }
}
