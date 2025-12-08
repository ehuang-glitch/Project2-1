package com.example.project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class StudentCheckboxAdapter extends RecyclerView.Adapter<StudentCheckboxAdapter.VH> {

    private List<StudentCheckItem> list = new ArrayList<>();

    public static class StudentCheckItem {
        public User user;
        public boolean isChecked;

        public StudentCheckItem(User user, boolean isChecked) {
            this.user = user;
            this.isChecked = isChecked;
        }
    }

    public void set(List<StudentCheckItem> items) {
        list.clear();
        if (items != null) {
            list.addAll(items);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedUserIds() {
        List<Integer> selected = new ArrayList<>();
        for (StudentCheckItem item : list) {
            if (item.isChecked) {
                selected.add(item.user.getId());
            }
        }
        return selected;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_checkbox, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StudentCheckItem item = list.get(position);

        holder.tvName.setText(item.user.getUsername());
        holder.checkbox.setChecked(item.isChecked);

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.isChecked = isChecked;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox checkbox;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvStudentName);
            checkbox = v.findViewById(R.id.cbStudent);
        }
    }
}
