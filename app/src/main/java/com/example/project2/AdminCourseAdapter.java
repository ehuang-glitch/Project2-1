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
    private OnCourseClickListener listener;

    public AdminCourseAdapter(OnCourseClickListener listener) {
        this.listener = listener;
    }

    public void set(List<Course> l) {
        list.clear();
        if (l != null) list.addAll(l);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        Course c = list.get(i);
        h.code.setText(c.getCode());
        h.name.setText(c.getName());

        h.itemView.setOnClickListener(v -> listener.onCourseClick(c));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView code, name;
        VH(View v) {
            super(v);
            code = v.findViewById(R.id.tvAdminCourseCode);
            name = v.findViewById(R.id.tvAdminCourseName);
        }
    }
}
