package com.example.finance.utils; // Murat Görkem Kahya worked on this page

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    private static final NumberFormat percentFormatter = NumberFormat.getPercentInstance(Locale.US);

    public static String formatCurrency(double amount) {
        return currencyFormatter.format(amount);
    }

    public static String formatCurrencyWithoutSymbol(double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    public static String formatPercent(double value) {
        percentFormatter.setMinimumFractionDigits(1);
        percentFormatter.setMaximumFractionDigits(1);
        return percentFormatter.format(value);
    }

    public static String formatCompact(double amount) {
        if (amount >= 1_000_000) {
            return String.format(Locale.US, "%.1fM", amount / 1_000_000);
        } else if (amount >= 1_000) {
            return String.format(Locale.US, "%.1fK", amount / 1_000);
        } else {
            return formatCurrency(amount);
        }
    }
}