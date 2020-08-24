package com.suri5.clubmngmt.Budget;

public class Budget {
    int key;
    String title;
    String date;
    boolean isIncome;
    int amount;
    String comment;

    public Budget(int key, String title, String date, boolean isIncome, int amount, String comment) {
        this.key = key;
        this.title = title;
        this.date = date;
        this.isIncome = isIncome;
        this.amount = amount;
        this.comment = comment;
    }

    public Budget() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
