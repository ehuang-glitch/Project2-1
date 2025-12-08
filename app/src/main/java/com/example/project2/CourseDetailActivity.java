package com.example.project2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.databinding.ActivityCourseDetailBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentDao;
import com.example.project2.database.dao.AssignmentSubmissionDao;
import com.example.project2.database.entities.Assignment;
import com.example.project2.database.entities.AssignmentSubmission;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private ActivityCourseDetailBinding binding;
    private int courseId, userId;
    private AssignmentAdapter allAssignmentsAdapter;
    private AssignmentAdapter pendingAssignmentsAdapter;
    private AssignmentDao assignmentDao;
    private AssignmentSubmissionDao submissionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getIntExtra("courseId", -1);
        userId = getIntent().getIntExtra("userId", -1);
        String courseName = getIntent().getStringExtra("courseName");
        String courseCode = getIntent().getStringExtra("courseCode");

        assignmentDao = AppDatabase.get(this).assignmentDao();
        submissionDao = AppDatabase.get(this).assignmentSubmissionDao();

        // Set header
        binding.tvCourseDetailName.setText(courseName);
        binding.tvCourseDetailCode.setText(courseCode);

        // Back button
        binding.btnBackCourseDetail.setOnClickListener(v -> finish());

        // Setup RecyclerViews
        binding.rvPendingAssignments.setLayoutManager(new LinearLayoutManager(this));
        pendingAssignmentsAdapter = new AssignmentAdapter(this, userId);
        binding.rvPendingAssignments.setAdapter(pendingAssignmentsAdapter);

        binding.rvAssignments.setLayoutManager(new LinearLayoutManager(this));
        allAssignmentsAdapter = new AssignmentAdapter(this, userId);
        binding.rvAssignments.setAdapter(allAssignmentsAdapter);

        loadAssignments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssignments();
    }

    private void loadAssignments() {
        AppDatabase.exec.execute(() -> {
            // Load all assignments
            List<Assignment> allAssignments = assignmentDao.getAssignmentsByCourse(courseId);
            
            // Separate into pending and submitted
            List<Assignment> pendingAssignments = new ArrayList<>();
            
            for (Assignment assignment : allAssignments) {
                AssignmentSubmission submission = submissionDao.getSubmission(assignment.getId(), userId);
                
                // If no submission or not submitted, it's pending
                if (submission == null || !submission.isSubmitted()) {
                    // Also check if overdue
                    if (System.currentTimeMillis() > assignment.getDueDate()) {
                        pendingAssignments.add(assignment);
                    }
                }
            }
            
            runOnUiThread(() -> {
                allAssignmentsAdapter.set(allAssignments);
                pendingAssignmentsAdapter.set(pendingAssignments);
                
                // Update counts
                binding.tvTotalCount.setText(String.valueOf(allAssignments.size()));
                binding.tvPendingCount.setText(String.valueOf(pendingAssignments.size()));
            });
        });
    }
}
