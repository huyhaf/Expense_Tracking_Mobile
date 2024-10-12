package com.map.hanhathuy.mobileapp.demo2.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.map.hanhathuy.mobileapp.demo2.model.Category;
import com.map.hanhathuy.mobileapp.demo2.model.CategoryInOut;
import com.map.hanhathuy.mobileapp.demo2.model.InOut;
import com.map.hanhathuy.mobileapp.demo2.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionDAO {
    private SQLiteDatabase db;
    private SimpleDateFormat dateFormat;

    public TransactionDAO(SQLiteDatabase db) {
        this.db = db;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public boolean add(Transaction t) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, t.getName());
        values.put(DatabaseHelper.COLUMN_CATEGORY_INOUT_ID, t.getCat().getId());
        values.put(DatabaseHelper.COLUMN_AMOUNT, t.getAmount());
        values.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(t.getDay()));
        values.put(DatabaseHelper.COLUMN_NOTE, t.getNote());

        long insertId = db.insert(DatabaseHelper.TABLE_TRANSACTION, null, values);
        return insertId != -1;
    }

    public boolean edit(Transaction t) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, t.getName());
        values.put(DatabaseHelper.COLUMN_CATEGORY_INOUT_ID, t.getCat().getId());
        values.put(DatabaseHelper.COLUMN_AMOUNT, t.getAmount());
        values.put(DatabaseHelper.COLUMN_DATE, dateFormat.format(t.getDay()));
        values.put(DatabaseHelper.COLUMN_NOTE, t.getNote());

        int rowsAffected = db.update(DatabaseHelper.TABLE_TRANSACTION, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(t.getId())});
        return rowsAffected > 0;
    }

    public boolean delete(int id) {
        int rowsAffected = db.delete(DatabaseHelper.TABLE_TRANSACTION,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public List<Transaction> search(Date day) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT t.*, c.name as category_name, i.name as inout_name, i.id as inout_id, c.icon as icon " +
                "FROM " + DatabaseHelper.TABLE_TRANSACTION + " t " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DatabaseHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORY + " c ON ci." + DatabaseHelper.COLUMN_CATEGORY_ID + " = c.id " +
                "JOIN " + DatabaseHelper.TABLE_INOUT + " i ON ci." + DatabaseHelper.COLUMN_INOUT_ID + " = i.id " +
                "WHERE t." + DatabaseHelper.COLUMN_DATE + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateFormat.format(day)});

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = cursorToTransaction(cursor);
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }

    @SuppressLint("Range")
    private Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        transaction.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
        transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT)));
        transaction.setNote(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE)));


        try {
            transaction.setDay(dateFormat.parse(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        CategoryInOut categoryInOut = new CategoryInOut();
        categoryInOut.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_INOUT_ID)));

        Category category = new Category();
        category.setName(cursor.getString(cursor.getColumnIndex("category_name")));
        category.setIcon(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ICON)));
        categoryInOut.setCategory(category);

        InOut inOut = new InOut();
        inOut.setId(cursor.getInt(cursor.getColumnIndex("inout_id")));
        inOut.setName(cursor.getString(cursor.getColumnIndex("inout_name")));
        categoryInOut.setIdInOut(inOut.getId());

        transaction.setCat(categoryInOut);

        return transaction;
    }

    public double getTotalIncome(Date date) {
        return getTotal(date, 1); // Assuming 1 is for income
    }

    public double getTotalExpense(Date date) {
        return getTotal(date, 2); // Assuming 2 is for expense
    }

    @SuppressLint("Range")
    private double getTotal(Date date, int inOutId) {
        String query = "SELECT SUM(t." + DatabaseHelper.COLUMN_AMOUNT + ") as total " +
                "FROM " + DatabaseHelper.TABLE_TRANSACTION + " t " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DatabaseHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "WHERE t." + DatabaseHelper.COLUMN_DATE + " = ? AND ci." + DatabaseHelper.COLUMN_INOUT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateFormat.format(date), String.valueOf(inOutId)});

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return total;
    }

    public int getIncomeCount(Date date) {
        return getCount(date, 1); // Giả sử 1 là ID cho thu nhập
    }

    public int getExpenseCount(Date date) {
        return getCount(date, 2); // Giả sử 2 là ID cho chi tiêu
    }

    @SuppressLint("Range")
    private int getCount(Date date, int inOutId) {
        String query = "SELECT COUNT(*) as count " +
                "FROM " + DatabaseHelper.TABLE_TRANSACTION + " t " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORY_INOUT + " ci ON t." + DatabaseHelper.COLUMN_CATEGORY_INOUT_ID + " = ci.id " +
                "WHERE t." + DatabaseHelper.COLUMN_DATE + " = ? AND ci." + DatabaseHelper.COLUMN_INOUT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dateFormat.format(date), String.valueOf(inOutId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return count;
    }
}
