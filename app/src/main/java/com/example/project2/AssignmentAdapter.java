package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentSubmissionDao;
import com.example.project2.database.entities.Assignment;
import com.example.project2.database.entities.AssignmentSubmission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.VH> {

    private List<Assignment> list = new ArrayList<>();
    private Context context;
    private int userId;
    private AssignmentSubmissionDao submissionDao;

    public AssignmentAdapter(Context context, int userId) {
        this.context = context;
        this.userId = userId;
        this.submissionDao = AppDatabase.get(context).assignmentSubmissionDao();
    }

    public void set(List<Assignment> assignments) {
        list.clear();
        if (assignments != null) {
            list.addAll(assignments);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Assignment assignment = list.get(position);

        holder.title.setText(assignment.getTitle());
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String dueDate = sdf.format(new Date(assignment.getDueDate()));
        holder.dueDate.setText("Due: " + dueDate);

        // Load submission and grade
        AppDatabase.exec.execute(() -> {
            AssignmentSubmission submission = submissionDao.getSubmission(assignment.getId(), userId);
            
            if (submission != null) {
                String status;
                int statusColor;
                
                if (submission.isSubmitted()) {
                    status = "✓ SUBMITTED";
                    statusColor = context.getColor(android.R.color.holo_green_dark);
                } else {
                    boolean isOverdue = System.currentTimeMillis() > assignment.getDueDate();
                    status = isOverdue ? "✗ OVERDUE" : "⏳ PENDING";
                    statusColor = isOverdue ? context.getColor(android.R.color.holo_red_dark) 
                                             : context.getColor(android.R.color.holo_orange_dark);
                }
                
                String grade;
                int gradeColor;
                if (submission.getGrade() >= 0) {
                    grade = submission.getGrade() + "/100";
                    gradeColor = context.getColor(android.R.color.holo_blue_dark);
                } else {
                    grade = "Not Graded";
                    gradeColor = context.getColor(android.R.color.holo_blue_light);
                }
                
                final int finalStatusColor = statusColor;
                final int finalGradeColor = gradeColor;
                final String finalStatus = status;
                final String finalGrade = grade;
                
                ((View) holder.status.getParent()).post(() -> {
                    holder.status.setText(finalStatus);
                    holder.status.setTextColor(finalStatusColor);
                    holder.grade.setText(finalGrade);
                    holder.grade.setTextColor(finalGradeColor);
                });
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AssignmentDetailActivity.class);
            intent.putExtra("assignmentId", assignment.getId());
            intent.putExtra("courseId", assignment.getCourseId());
            intent.putExtra("userId", userId);
            intent.putExtra("assignmentTitle", assignment.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, dueDate, status, grade;

        VH(View v) {
            super(v);
            title = v.findViewById(R.id.tvAssignmentTitle);
            dueDate = v.findViewById(R.id.tvAssignmentDueDate);
            status = v.findViewById(R.id.tvAssignmentStatus);
            grade = v.findViewById(R.id.tvAssignmentGrade);
        }
    }
}
