package com.example.project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.entities.Assignment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssignmentManagementAdapter extends RecyclerView.Adapter<AssignmentManagementAdapter.VH> {

    private List<Assignment> list = new ArrayList<>();
    private OnAssignmentActionListener listener;

    public interface OnAssignmentActionListener {
        void onEditAssignment(Assignment assignment);
        void onDeleteAssignment(Assignment assignment);
        void onGradeAssignment(Assignment assignment);
    }

    public AssignmentManagementAdapter(OnAssignmentActionListener listener) {
        this.listener = listener;
    }

    public void set(List<Assignment> items) {
        list.clear();
        if (items != null) {
            list.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment_management, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Assignment assignment = list.get(position);

        holder.tvTitle.setText(assignment.getTitle());
        holder.tvDescription.setText(assignment.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String dueDate = sdf.format(new Date(assignment.getDueDate()));
        holder.tvDueDate.setText("Due: " + dueDate);

        holder.btnEdit.setOnClickListener(v -> listener.onEditAssignment(assignment));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteAssignment(assignment));
        holder.btnGrade.setOnClickListener(v -> listener.onGradeAssignment(assignment));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDueDate;
        Button btnEdit, btnDelete, btnGrade;

        VH(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvAssignmentTitle);
            tvDescription = v.findViewById(R.id.tvAssignmentDescription);
            tvDueDate = v.findViewById(R.id.tvDueDate);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnGrade = v.findViewById(R.id.btnGrade);
        }
    }
}
