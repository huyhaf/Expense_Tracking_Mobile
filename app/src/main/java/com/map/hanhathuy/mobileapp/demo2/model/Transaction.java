package com.map.hanhathuy.mobileapp.demo2.model;

import java.util.Date;

public class Transaction {
    private int id;
    private String name;
    private CategoryInOut cat;
    private double amount;
    private Date day;
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryInOut getCat() {
        return cat;
    }

    public void setCat(CategoryInOut cat) {
        this.cat = cat;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
