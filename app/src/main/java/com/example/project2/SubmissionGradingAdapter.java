package com.example.project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.entities.AssignmentSubmission;
import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SubmissionGradingAdapter extends RecyclerView.Adapter<SubmissionGradingAdapter.VH> {

    public static class SubmissionWithStudent {
        public User student;
        public AssignmentSubmission submission;

        public SubmissionWithStudent(User student, AssignmentSubmission submission) {
            this.student = student;
            this.submission = submission;
        }
    }

    private List<SubmissionWithStudent> list = new ArrayList<>();
    private OnSubmissionActionListener listener;

    public interface OnSubmissionActionListener {
        void onMarkDone(SubmissionWithStudent item);
        void onGradeStudent(SubmissionWithStudent item);
    }

    public SubmissionGradingAdapter(OnSubmissionActionListener listener) {
        this.listener = listener;
    }

    public void set(List<SubmissionWithStudent> items) {
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
                .inflate(R.layout.item_student_submission, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        SubmissionWithStudent item = list.get(position);
        AssignmentSubmission submission = item.submission;
        User student = item.student;

        holder.tvStudentName.setText(student.getUsername());

        if (submission.isSubmitted()) {
            holder.tvSubmissionStatus.setText("✓ Submitted");
            holder.tvSubmissionStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvSubmissionStatus.setText("✗ Not Submitted");
            holder.tvSubmissionStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_orange_dark));
        }

        if (submission.getGrade() >= 0) {
            holder.tvGrade.setText(submission.getGrade() + "/100");
            holder.tvGrade.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_blue_dark));
        } else {
            holder.tvGrade.setText("Pending");
            holder.tvGrade.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_blue_light));
        }

        holder.btnMarkDone.setOnClickListener(v -> listener.onMarkDone(item));
        holder.btnGrade.setOnClickListener(v -> listener.onGradeStudent(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvSubmissionStatus, tvGrade;
        Button btnMarkDone, btnGrade;

        VH(View v) {
            super(v);
            tvStudentName = v.findViewById(R.id.tvStudentName);
            tvSubmissionStatus = v.findViewById(R.id.tvSubmissionStatus);
            tvGrade = v.findViewById(R.id.tvGrade);
            btnMarkDone = v.findViewById(R.id.btnMarkDone);
            btnGrade = v.findViewById(R.id.btnGradeStudent);
        }
    }
}
