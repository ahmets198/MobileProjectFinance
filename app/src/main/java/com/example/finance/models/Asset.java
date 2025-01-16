package com.example.finance.models;

public class Asset {
    private long id;
    private String type;
    private double amount;
    private double worth;

    public Asset(String type, double amount, double worth) {
        this.type = type;
        this.amount = amount;
        this.worth = worth;
    }

    // Constructor with ID for database operations
    public Asset(long id, String type, double amount, double worth) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.worth = worth;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getWorth() { return worth; }
    public void setWorth(double worth) { this.worth = worth; }
}