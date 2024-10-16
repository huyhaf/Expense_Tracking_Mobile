package com.map.hanhathuy.mobileapp.demo2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.map.hanhathuy.mobileapp.demo2.R;
import com.map.hanhathuy.mobileapp.demo2.model.DailyStat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<DailyStat> monthlyStats;
    private Calendar displayCalendar;
    private LayoutInflater inflater;

    public CalendarAdapter(Context context, List<DailyStat> monthlyStats, Date displayDate) {
        this.context = context;
        this.monthlyStats = monthlyStats;
        this.displayCalendar = Calendar.getInstance();
        this.displayCalendar.setTime(displayDate);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 42; // 6 rows, 7 days each
    }

    @Override
    public Object getItem(int position) {
        Calendar calendar = (Calendar) displayCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, position - calendar.get(Calendar.DAY_OF_WEEK) + 1);
        return calendar.getTime();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_calendar_day_layout, parent, false);
        }

        TextView textDay = convertView.findViewById(R.id.textDay);
        TextView textIncome = convertView.findViewById(R.id.textIncome);
        TextView textOutcome = convertView.findViewById(R.id.textOutcome);

        Date date = (Date) getItem(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        textDay.setText(String.valueOf(dayOfMonth));

        boolean isCurrentMonth = calendar.get(Calendar.MONTH) == displayCalendar.get(Calendar.MONTH);
        textDay.setTextColor(isCurrentMonth ? Color.BLACK : Color.GRAY);

        textIncome.setVisibility(View.GONE);
        textOutcome.setVisibility(View.GONE);

        if (isCurrentMonth && dayOfMonth <= monthlyStats.size()) {
            DailyStat dailyStat = monthlyStats.get(dayOfMonth - 1);

            if (dailyStat.getIncome() > 0) {
                textIncome.setText("+" + formatNumber(dailyStat.getIncome()));
                textIncome.setVisibility(View.VISIBLE);
            }

            if (dailyStat.getOutcome() > 0) {
                textOutcome.setText("-" + formatNumber(dailyStat.getOutcome()));
                textOutcome.setVisibility(View.VISIBLE);
            }
        }

        convertView.setOnClickListener(v -> {
            if (isCurrentMonth) {
                ((GridView) parent).performItemClick(v, position, getItemId(position));
            }
        });

        return convertView;
    }

    private String formatNumber(double number) {
        if (number >= 1000000000) {
            return String.format("%.1fB", number / 1000000000);
        } else if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000);
        } else {
            return String.format("%.0f", number);
        }
    }
}