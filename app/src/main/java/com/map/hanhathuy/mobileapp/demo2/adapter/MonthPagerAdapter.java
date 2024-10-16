package com.map.hanhathuy.mobileapp.demo2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.map.hanhathuy.mobileapp.demo2.R;
import com.map.hanhathuy.mobileapp.demo2.dao.DailyStatDAO;
import com.map.hanhathuy.mobileapp.demo2.model.DailyStat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthPagerAdapter extends RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder> {

    public static final int INITIAL_POSITION = 1000;
    private static final int TOTAL_PAGES = 2000;

    private Context context;
    private DailyStatDAO dailyStatDAO;
    private OnDayClickListener onDayClickListener;

    public interface OnDayClickListener {
        void onDayClick(Date date);
    }

    public MonthPagerAdapter(Context context, DailyStatDAO dailyStatDAO, OnDayClickListener listener) {
        this.context = context;
        this.dailyStatDAO = dailyStatDAO;
        this.onDayClickListener = listener;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.month_grid_layout, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position - INITIAL_POSITION);
        Date monthDate = calendar.getTime();

        List<DailyStat> monthlyStats = dailyStatDAO.getMonthlyStat(monthDate);
        CalendarAdapter adapter = new CalendarAdapter(context, monthlyStats, monthDate);
        holder.gridView.setAdapter(adapter);

        holder.gridView.setOnItemClickListener((parent, view, pos, id) -> {
            Date clickedDate = (Date) adapter.getItem(pos);
            if (onDayClickListener != null) {
                onDayClickListener.onDayClick(clickedDate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return TOTAL_PAGES;
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder {
        GridView gridView;

        MonthViewHolder(View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gridCalendar);
        }
    }
}