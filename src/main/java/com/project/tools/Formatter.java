package com.project.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Formatter {
    public static String formatDate(String dateStr, String inputFormat, String outputFormat) throws Exception {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
        try {
            Date parsedDate = inputDateFormat.parse(dateStr);
            return outputDateFormat.format(parsedDate);
        } catch (Exception e) {
            throw e;
        }
    }

    public static java.sql.Date addDays(java.sql.Date date, int days) {
        LocalDate localDate = date.toLocalDate();
        LocalDate newDate = localDate.plusDays(days);
        java.sql.Date outputDate = java.sql.Date.valueOf(newDate);
        return outputDate;
    }

    public static String formatThousand(double number) {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(number);
    }

    public static String formatThousand(float number) {
        DecimalFormat format = new DecimalFormat("#,###.##");
        return format.format(number);
    }

    @SuppressWarnings("deprecation")
    public static String formatDate(Date date) {
        return date.toLocaleString().replaceAll("00:00:00", "");
    }

    public static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().replaceAll("'", "''"));
                currentValue.setLength(0); // Réinitialiser la chaîne pour la prochaine valeur
            } else {
                currentValue.append(c);
            }
        }

        values.add(currentValue.toString()); // Ajouter la dernière valeur
        return values.toArray(new String[0]);
    }

}
