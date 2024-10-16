package com.map.hanhathuy.mobileapp.demo2.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.map.hanhathuy.mobileapp.demo2.model.DailyStat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyStatDAO {
    private SQLiteDatabase db;
    private TransactionDAO transactionDAO;

    public DailyStatDAO(SQLiteDatabase db) {
        this.db = db;
        transactionDAO = new TransactionDAO(db);
    }

    public DailyStat getDailyStat(Date day) {
        return new DailyStat(day, transactionDAO.getTotalIncome(day), transactionDAO.getTotalExpense(day));
    }

    public List<DailyStat> getMonthlyStat(Date date) {
        List<DailyStat> monthlyStats = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < daysInMonth; i++) {
            Date currentDay = cal.getTime();
            monthlyStats.add(getDailyStat(currentDay));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return monthlyStats;
    }
}