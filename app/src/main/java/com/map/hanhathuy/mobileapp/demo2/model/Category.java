package com.map.hanhathuy.mobileapp.demo2.model;

import androidx.annotation.NonNull;

public class Category {
    private int id;
    private String name;
    private int parent;
    private String icon;
    private String note;
    private CategoryInOut categoryInOut;

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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CategoryInOut getCategoryInOut() {
        return categoryInOut;
    }

    public void setCategoryInOut(CategoryInOut categoryInOut) {
        this.categoryInOut = categoryInOut;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
