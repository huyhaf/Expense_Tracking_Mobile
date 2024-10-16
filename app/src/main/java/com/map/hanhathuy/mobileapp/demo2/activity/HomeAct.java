package com.map.hanhathuy.mobileapp.demo2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.map.hanhathuy.mobileapp.demo2.R;
import com.map.hanhathuy.mobileapp.demo2.adapter.TransactionAdapter;
import com.map.hanhathuy.mobileapp.demo2.dao.DatabaseHelper;
import com.map.hanhathuy.mobileapp.demo2.dao.TransactionDAO;
import com.map.hanhathuy.mobileapp.demo2.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeAct extends AppCompatActivity {

    private TextView textViewDate;
    private Button buttonTongThu, buttonTongChi, buttonThemGiaoDich;
    private ListView listViewTransactions;
    private TextView textViewTransaction;
    private ImageButton buttonCalendar;
    private Date currentDisplayDate;

    private TransactionDAO transactionDAO;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        textViewDate = findViewById(R.id.textDate);
        buttonTongThu = findViewById(R.id.buttonTongThu);
        buttonTongChi = findViewById(R.id.buttonTongChi);
        listViewTransactions = findViewById(R.id.listViewTransactions);
        buttonThemGiaoDich = findViewById(R.id.buttonThemGiaoDich);
        textViewTransaction = findViewById(R.id.textCountTransaction);
        buttonThemGiaoDich = findViewById(R.id.buttonThemGiaoDich);
        buttonCalendar = findViewById(R.id.buttonCalendar);

        // Check if a date was passed from MonthlyViewActivity
        long selectedDateMillis = getIntent().getLongExtra("SELECTED_DATE", -1);
        if (selectedDateMillis != -1) {
            currentDisplayDate = new Date(selectedDateMillis);
        } else {
            currentDisplayDate = new Date();
        }

        // Set current date
        setCurrentDate();

        // Initialize DAO
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        transactionDAO = new TransactionDAO(dbHelper.getReadableDatabase());

        // Load transactions
        loadTransactions();

        // Set up button click listeners
        setupButtonListeners();
    }

    private void setCurrentDate() {
        if (textViewDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(currentDisplayDate);
            textViewDate.setText(currentDate);
        }
    }

    private void loadTransactions() {
        List<Transaction> transactions = transactionDAO.search(currentDisplayDate);
        transactionAdapter = new TransactionAdapter(this, transactions);
        listViewTransactions.setAdapter(transactionAdapter);
        if (textViewTransaction != null) {
            textViewTransaction.setText(String.format(Locale.getDefault(), "%d giao dịch", transactions.size()));
        }
    }

    private void setupButtonListeners() {
        buttonTongThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalIncome = transactionDAO.getTotalIncome(currentDisplayDate);
                int incomeCount = transactionDAO.getIncomeCount(currentDisplayDate);
                buttonTongThu.setText(String.format(Locale.getDefault(), "Tổng thu: %.2f (%d lần)", totalIncome, incomeCount));
                buttonTongThu.setTextColor(Color.GREEN);
            }
        });

        buttonTongChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalExpense = transactionDAO.getTotalExpense(currentDisplayDate);
                int expenseCount = transactionDAO.getExpenseCount(currentDisplayDate);
                buttonTongChi.setText(String.format(Locale.getDefault(), "Tổng chi: %.2f (%d lần)", totalExpense, expenseCount));
                buttonTongChi.setTextColor(Color.RED);
            }
        });

        buttonThemGiaoDich.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAct.this, AddTransactionAct.class);
            startActivity(intent);
        });

        buttonCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAct.this, MonthlyViewAct.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload transactions when returning to this activity
        loadTransactions();
    }
}