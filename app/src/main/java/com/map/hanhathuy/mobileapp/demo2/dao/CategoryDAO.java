package com.map.hanhathuy.mobileapp.demo2.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.map.hanhathuy.mobileapp.demo2.model.Category;
import com.map.hanhathuy.mobileapp.demo2.model.CategoryInOut;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private SQLiteDatabase db;

    public CategoryDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public List<CategoryInOut> searchByInOut(boolean isIn) {
        List<CategoryInOut> categories = new ArrayList<>();
        String query = "SELECT c.*, ci." + DatabaseHelper.COLUMN_INOUT_ID + " FROM " + DatabaseHelper.TABLE_CATEGORY + " c " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORY_INOUT + " ci ON c." + DatabaseHelper.COLUMN_ID + " = ci." + DatabaseHelper.COLUMN_CATEGORY_ID + " " +
                "WHERE ci." + DatabaseHelper.COLUMN_INOUT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(isIn ? 1 : 2)});

        if (cursor.moveToFirst()) {
            do {
                CategoryInOut categoryInOut = cursorToCategoryInOut(cursor);
                categories.add(categoryInOut);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public ArrayList<Category> getAllParentCategories() {
        ArrayList<Category> parentCategories = new ArrayList<>();

        String query = "SELECT * FROM "+DatabaseHelper.TABLE_CATEGORY+" WHERE "+DatabaseHelper.COLUMN_PARENT_ID+" IS NULL OR "+DatabaseHelper.COLUMN_PARENT_ID+" = 0";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
                category.setParent(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PARENT_ID)));
                category.setNote(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE)));
                category.setIcon(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ICON)));
                parentCategories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return parentCategories;
    }

    @SuppressLint("Range")
    private CategoryInOut cursorToCategoryInOut(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
        category.setNote(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE)));
        category.setIcon(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ICON)));

        int parentId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PARENT_ID));
        if (parentId != 0) {
            Category parent = getCategoryById(parentId);
            category.setParent(parent.getId());
        }

        CategoryInOut categoryInOut = new CategoryInOut();
        categoryInOut.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        categoryInOut.setIdInOut(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_INOUT_ID)));
        categoryInOut.setCategory(category);

        return categoryInOut;
    }

    @SuppressLint("Range")
    private Category getCategoryById(int id) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CATEGORY + " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        Category category = null;
        if (cursor.moveToFirst()) {
            category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
            category.setNote(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE)));
        }
        cursor.close();
        return category;
    }

    public boolean add(Category category,DatabaseHelper databaseHelper) {
        db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("idParent", category.getParent());
        values.put("note", category.getNote());
        values.put("icon",category.getIcon());

        long categoryId = db.insert("category", null, values);

        if (categoryId != -1) {
            // Category inserted successfully, now insert into categoryInOut
            ContentValues categoryInOutValues = new ContentValues();
            categoryInOutValues.put("idCategory", categoryId);
            categoryInOutValues.put("idInOut", category.getCategoryInOut().getIdInOut());

            long categoryInOutId = db.insert("categoryInOut", null, categoryInOutValues);

            return categoryInOutId != -1;
        }

        return false;
    }
}