package com.map.hanhathuy.mobileapp.demo2.activity;

import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.map.hanhathuy.mobileapp.demo2.R;
import com.map.hanhathuy.mobileapp.demo2.adapter.IconSpinnerAdapter;
import com.map.hanhathuy.mobileapp.demo2.dao.CategoryDAO;
import com.map.hanhathuy.mobileapp.demo2.dao.DatabaseHelper;
import com.map.hanhathuy.mobileapp.demo2.model.Category;
import com.map.hanhathuy.mobileapp.demo2.model.CategoryInOut;
import com.map.hanhathuy.mobileapp.demo2.model.InOut;

import java.util.ArrayList;

public class AddCategoryAct extends AppCompatActivity {

    private EditText etCategoryName;
    private Button btnAddCategory, btnResetCategory;
    private Spinner spIcon, spParent, spInOut;

    private CategoryDAO categoryDAO;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        dbHelper = new DatabaseHelper(this);
        etCategoryName = findViewById(R.id.etName);
        btnAddCategory = findViewById(R.id.btnAdd);
        btnResetCategory = findViewById(R.id.btnReset);
        spIcon = findViewById(R.id.spIcon);
        spInOut = findViewById(R.id.spInOut);
        spParent = findViewById(R.id.spParent);
        categoryDAO = new CategoryDAO(dbHelper.getReadableDatabase());
        setupSpinners();

        btnAddCategory.setOnClickListener(v -> saveCategory());
        btnResetCategory.setOnClickListener(v -> resetCategory());
    }

    private void saveCategory() {
        String name = etCategoryName.getText().toString().trim();
        String inOut = spInOut.getSelectedItem().toString();
        int iconResourceId = (Integer) spIcon.getSelectedItem();
        Category parentCategory = (Category) spParent.getSelectedItem();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }

        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setParent(parentCategory != null ? parentCategory.getId() : 0);
        newCategory.setIcon(String.valueOf(iconResourceId));

        CategoryInOut categoryInOut = new CategoryInOut();
        InOut inOutObj = new InOut();
        inOutObj.setId(inOut.equals("Income") ? 1 : 2);
        inOutObj.setName(inOut);
        categoryInOut.setIdInOut(inOutObj.getId());
        newCategory.setCategoryInOut(categoryInOut);

        boolean success = categoryDAO.add(newCategory, dbHelper);
        if (success) {
            Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetCategory() {
        etCategoryName.setText("");
        spInOut.setSelection(0);
        spIcon.setSelection(0);
        spParent.setSelection(0);
    }

    private void setupSpinners() {
        // Setup In/Out spinner
        ArrayAdapter<CharSequence> inOutAdapter = ArrayAdapter.createFromResource(this,
                R.array.in_out_array, android.R.layout.simple_spinner_item);
        inOutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInOut.setAdapter(inOutAdapter);

        // Setup Icon spinner
        TypedArray iconArray = getResources().obtainTypedArray(R.array.icon_array);
        Integer[] iconIds = new Integer[iconArray.length()];
        for (int i = 0; i < iconArray.length(); i++) {
            iconIds[i] = iconArray.getResourceId(i, 0);
        }
        iconArray.recycle();

        IconSpinnerAdapter iconAdapter = new IconSpinnerAdapter(this, iconIds);
        spIcon.setAdapter(iconAdapter);

        // Setup Parent spinner
        ArrayList<Category> parentCategories = categoryDAO.getAllParentCategories();
        ArrayAdapter<Category> parentAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, parentCategories);
        parentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spParent.setAdapter(parentAdapter);
    }
}