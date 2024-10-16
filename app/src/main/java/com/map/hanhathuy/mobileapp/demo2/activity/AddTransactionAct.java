package com.map.hanhathuy.mobileapp.demo2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.map.hanhathuy.mobileapp.demo2.R;
import com.map.hanhathuy.mobileapp.demo2.adapter.CategoryAdapter;
import com.map.hanhathuy.mobileapp.demo2.dao.CategoryDAO;
import com.map.hanhathuy.mobileapp.demo2.dao.DatabaseHelper;
import com.map.hanhathuy.mobileapp.demo2.dao.TransactionDAO;
import com.map.hanhathuy.mobileapp.demo2.model.CategoryInOut;
import com.map.hanhathuy.mobileapp.demo2.model.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransactionAct extends AppCompatActivity {
    private RadioButton radioThu, radioChi;
    private Spinner spCategory;
    private EditText etAmount, etNote;
    private DatePicker dpDate;
    private Button btnAdd,btnAddCategory;
    private CategoryDAO categoryDAO;
    private TransactionDAO transactionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        initializeViews();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        categoryDAO = new CategoryDAO(dbHelper.getReadableDatabase());
        transactionDAO = new TransactionDAO(dbHelper.getWritableDatabase());

        setupCategorySpinner();

        btnAdd.setOnClickListener(v -> addTransaction());

        btnAddCategory.setOnClickListener(v->{
            Intent intent = new Intent(AddTransactionAct.this, AddCategoryAct.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        radioThu = findViewById(R.id.rbIn);
        radioChi = findViewById(R.id.rbOut);
        spCategory = findViewById(R.id.spCategory);
        etAmount = findViewById(R.id.etAmount);
        etNote = findViewById(R.id.etNote);
        dpDate = findViewById(R.id.dpDate);
        btnAdd = findViewById(R.id.btnAdd);
        btnAddCategory = findViewById(R.id.btnAddCategory);
    }

    private void setupCategorySpinner() {
        radioThu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadCategories(true);
            }
        });

        radioChi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadCategories(false);
            }
        });

        // Default to "Thu" categories
        radioThu.setChecked(true);
    }

    private void loadCategories(boolean isIncome) {
        List<CategoryInOut> categories = categoryDAO.searchByInOut(isIncome);
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        spCategory.setAdapter(adapter);
    }

    private void addTransaction() {
        try {
            CategoryInOut selectedCategoryInOut = (CategoryInOut) spCategory.getSelectedItem();
            double amount = Double.parseDouble(etAmount.getText().toString());
            String note = etNote.getText().toString();

            int day = dpDate.getDayOfMonth();
            int month = dpDate.getMonth();
            int year = dpDate.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();

            Transaction transaction = new Transaction();
            transaction.setCat(selectedCategoryInOut);
            transaction.setAmount(amount);
            transaction.setDay(date);
            transaction.setNote(note);

            boolean success = transactionDAO.add(transaction);

            if (success) {
                Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTransactionAct.this,HomeAct.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
