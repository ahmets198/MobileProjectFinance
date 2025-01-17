package com.example.finance.models; // Ahmet Sazan worked on this page

public class Goal {
    private long id;
    private String description;
    private double targetAmount;
    private double currentAmount;
    private String deadline;
    private String category;
    private int priority;

    public Goal(String description, double targetAmount, double currentAmount) {
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    public Goal(long id, String description, double targetAmount, double currentAmount,
                String deadline, String category, int priority) {
        this.id = id;
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.category = category;
        this.priority = priority;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    // Utility methods
    public double getProgress() {
        return (currentAmount / targetAmount) * 100;
    }

    public boolean isCompleted() {
        return currentAmount >= targetAmount;
    }

    public double getRemainingAmount() {
        return targetAmount - currentAmount;
    }
}