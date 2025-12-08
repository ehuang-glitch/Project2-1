package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.entities.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH> {
    
    private List<Course> list = new ArrayList<>();
    private Context context;
    private int userId;

    public CourseAdapter(Context context, int userId) {
        this.context = context;
        this.userId = userId;
    }

    public void set(List<Course> l) {
        list.clear();
        if (l != null) list.addAll(l);
        notifyDataSetChanged();
    }

    @NonNull
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_course_card, p, false));
    }

    public void onBindViewHolder(@NonNull VH h, int i) {
        Course c = list.get(i);
        h.code.setText(c.getCode());
        h.name.setText(c.getName());
        h.progressBar.setProgress(0);

        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("courseId", c.getId());
            intent.putExtra("userId", userId);
            intent.putExtra("courseName", c.getName());
            intent.putExtra("courseCode", c.getCode());
            context.startActivity(intent);
        });
    }

    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView code, name;
        ProgressBar progressBar;

        VH(View v) {
            super(v);
            code = v.findViewById(R.id.tvCourseCode);
            name = v.findViewById(R.id.tvCourseName);
            progressBar = v.findViewById(R.id.progressBarGrade);
        }
    }
}
