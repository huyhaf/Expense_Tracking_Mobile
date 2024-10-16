package com.map.hanhathuy.mobileapp.demo2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.map.hanhathuy.mobileapp.demo2.R;
import com.map.hanhathuy.mobileapp.demo2.adapter.MonthPagerAdapter;
import com.map.hanhathuy.mobileapp.demo2.dao.DailyStatDAO;
import com.map.hanhathuy.mobileapp.demo2.dao.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthlyViewAct extends AppCompatActivity implements MonthPagerAdapter.OnDayClickListener {

    private TextView textMonthYear;
    private ViewPager2 viewPagerMonths;
    private DailyStatDAO dailyStatDAO;
    private Calendar currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_view);

        textMonthYear = findViewById(R.id.textMonthYear);
        viewPagerMonths = findViewById(R.id.viewPagerMonths);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dailyStatDAO = new DailyStatDAO(dbHelper.getReadableDatabase());

        currentDate = Calendar.getInstance();
        setupViewPager();

        findViewById(R.id.buttonBackToHome).setOnClickListener(v -> finish());
    }

    private void setupViewPager() {
        MonthPagerAdapter adapter = new MonthPagerAdapter(this, dailyStatDAO, this);
        viewPagerMonths.setAdapter(adapter);
        viewPagerMonths.setCurrentItem(MonthPagerAdapter.INITIAL_POSITION, false);

        viewPagerMonths.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateMonthYearText(position - MonthPagerAdapter.INITIAL_POSITION);
            }
        });

        updateMonthYearText(0);
    }

    private void updateMonthYearText(int monthOffset) {
        Calendar calendar = (Calendar) currentDate.clone();
        calendar.add(Calendar.MONTH, monthOffset);
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        textMonthYear.setText(monthYearFormat.format(calendar.getTime()));
    }

    @Override
    public void onDayClick(Date date) {
        Intent intent = new Intent(this, HomeAct.class);
        intent.putExtra("SELECTED_DATE", date.getTime());
        startActivity(intent);
    }
}