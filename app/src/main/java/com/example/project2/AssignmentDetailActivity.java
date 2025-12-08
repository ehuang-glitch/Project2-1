package com.example.project2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.databinding.ActivityAssignmentDetailBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentDao;
import com.example.project2.database.dao.AssignmentSubmissionDao;
import com.example.project2.database.entities.Assignment;
import com.example.project2.database.entities.AssignmentSubmission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AssignmentDetailActivity extends AppCompatActivity {

    private ActivityAssignmentDetailBinding binding;
    private int assignmentId, userId, courseId;
    private AssignmentSubmissionDao submissionDao;
    private AssignmentDao assignmentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assignmentId = getIntent().getIntExtra("assignmentId", -1);
        userId = getIntent().getIntExtra("userId", -1);
        courseId = getIntent().getIntExtra("courseId", -1);
        String assignmentTitle = getIntent().getStringExtra("assignmentTitle");

        submissionDao = AppDatabase.get(this).assignmentSubmissionDao();
        assignmentDao = AppDatabase.get(this).assignmentDao();

        binding.tvAssignmentDetailTitle.setText(assignmentTitle);

        // Load assignment details
        AppDatabase.exec.execute(() -> {
            Assignment assignment = assignmentDao.getAssignmentById(assignmentId);

            if (assignment != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                String dueDate = sdf.format(new Date(assignment.getDueDate()));

                runOnUiThread(() -> {
                    binding.tvAssignmentDetailDescription.setText(assignment.getDescription());
                    binding.tvAssignmentDetailDueDate.setText("Due: " + dueDate);
                });
            }

            // Load submission status and grade
            AssignmentSubmission submission = submissionDao.getSubmission(assignmentId, userId);

            if (submission == null) {
                // Create new submission record
                submission = new AssignmentSubmission(assignmentId, userId);
                submissionDao.insert(submission);
            }

            final AssignmentSubmission finalSubmission = submission;

            runOnUiThread(() -> {
                updateSubmissionStatus(finalSubmission);
            });
        });

        // Submit button click
        binding.btnSubmitAssignment.setOnClickListener(v -> {
            AppDatabase.exec.execute(() -> {
                AssignmentSubmission submission = submissionDao.getSubmission(assignmentId, userId);

                if (submission != null) {
                    submission.setSubmitted(true);
                    submission.setSubmittedDate(System.currentTimeMillis());
                    submissionDao.update(submission);

                    runOnUiThread(() -> {
                        Toast.makeText(AssignmentDetailActivity.this, 
                                "Assignment submitted successfully!", 
                                Toast.LENGTH_SHORT).show();
                        updateSubmissionStatus(submission);
                        binding.btnSubmitAssignment.setEnabled(false);
                        binding.btnSubmitAssignment.setAlpha(0.5f);
                    });
                }
            });
        });

        // Back button
        binding.btnBackAssignmentDetail.setOnClickListener(v -> finish());
    }

    private void updateSubmissionStatus(AssignmentSubmission submission) {
        // Update status
        if (submission.isSubmitted()) {
            binding.tvSubmissionStatus.setText("✓ SUBMITTED");
            binding.tvSubmissionStatus.setTextColor(getColor(android.R.color.holo_green_dark));
            binding.btnSubmitAssignment.setEnabled(false);
            binding.btnSubmitAssignment.setAlpha(0.5f);
        } else {
            binding.tvSubmissionStatus.setText("✗ PENDING");
            binding.tvSubmissionStatus.setTextColor(getColor(android.R.color.holo_orange_dark));
            binding.btnSubmitAssignment.setEnabled(true);
            binding.btnSubmitAssignment.setAlpha(1f);
        }

        // Update grade
        if (submission.getGrade() >= 0) {
            binding.tvGrade.setText(submission.getGrade() + "/100");
            binding.tvGrade.setTextColor(getColor(android.R.color.holo_blue_dark));
        } else {
            binding.tvGrade.setText("Not Graded");
            binding.tvGrade.setTextColor(getColor(android.R.color.holo_blue_light));
        }
    }
}
