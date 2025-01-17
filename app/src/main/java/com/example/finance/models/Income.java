package com.example.finance.models; // Murat Görkem Kahya worked on this page

public class Income {
    private long id;
    private String source;
    private double amount;
    private String date;
    private String category;

    public Income(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    public Income(long id, String source, double amount, String date, String category) {
        this.id = id;
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}