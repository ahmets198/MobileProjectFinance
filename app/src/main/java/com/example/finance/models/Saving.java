package com.example.finance.models; // Ahmet Sazan worked on this page

public class Saving {
    private long id;
    private String type;
    private double amount;
    private double returnRate;
    private String startDate;
    private String targetDate;
    private double targetAmount;

    public Saving(String type, double amount, double returnRate) {
        this.type = type;
        this.amount = amount;
        this.returnRate = returnRate;
    }

    public Saving(long id, String type, double amount, double returnRate,
                  String startDate, String targetDate, double targetAmount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.returnRate = returnRate;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.targetAmount = targetAmount;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getReturnRate() { return returnRate; }
    public void setReturnRate(double returnRate) { this.returnRate = returnRate; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getTargetDate() { return targetDate; }
    public void setTargetDate(String targetDate) { this.targetDate = targetDate; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    // Utility method to calculate current progress
    public double getProgress() {
        return (amount / targetAmount) * 100;
    }
}