package com.example.project2;

import static androidx.collection.ScatterMapKt.get;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    public List<Course> courseList;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your custom layout "item_course_card"
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        // Bind data to the views defined in item_course_card.xml
        holder.tvCourseCode.setText(course.getCourseCode());
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvCourseGrade.setText(course.getGradeString());
        holder.progressBarGrade.setProgress(course.getGradeProgress());
        holder.tvAssignmentsList.setText(course.getAssignments());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    // ViewHolder class to hold references to your UI elements
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseCode, tvCourseName, tvCourseGrade, tvAssignmentsList;
        ProgressBar progressBarGrade;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Match these IDs with item_course_card.xml
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseGrade = itemView.findViewById(R.id.tvCourseGrade);
            progressBarGrade = itemView.findViewById(R.id.progressBarGrade);
            tvAssignmentsList = itemView.findViewById(R.id.tvAssignmentsList);
        }
    }
}