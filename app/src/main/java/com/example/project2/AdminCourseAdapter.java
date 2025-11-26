package com.example.project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.entities.Course;

import java.util.ArrayList;
import java.util.List;

public class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.VH> {

    public interface OnCourseClickListener {
        void onCourseClick(Course c);
    }

    private List<Course> list = new ArrayList<>();
    private final OnCourseClickListener listener;

    public AdminCourseAdapter(OnCourseClickListener listener) {
        this.listener = listener;
    }

    public void set(List<Course> courses) {
        list.clear();
        if (courses != null)
            list.addAll(courses);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_course, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Course c = list.get(position);

        holder.code.setText(c.getCode());
        holder.name.setText(c.getName());
        holder.userId.setText("User: " + c.getUserId());
        holder.percentage.setText(c.getPercentage() + "%");

        holder.itemView.setOnClickListener(v -> listener.onCourseClick(c));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView code, name, userId, percentage;

        VH(View v) {
            super(v);
            code = v.findViewById(R.id.tvAdminCourseCode);
            name = v.findViewById(R.id.tvAdminCourseName);
            userId = v.findViewById(R.id.tvAdminCourseUserId);
            percentage = v.findViewById(R.id.tvAdminCoursePercentage);
        }
    }
}
