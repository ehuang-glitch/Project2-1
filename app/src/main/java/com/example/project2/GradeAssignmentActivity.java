package com.example.project2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.databinding.ActivityGradeAssignmentBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentSubmissionDao;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.AssignmentSubmission;
import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class GradeAssignmentActivity extends AppCompatActivity implements SubmissionGradingAdapter.OnSubmissionActionListener {

    private ActivityGradeAssignmentBinding binding;
    private SubmissionGradingAdapter adapter;
    private int assignmentId;
    private String assignmentTitle;
    private AssignmentSubmissionDao submissionDao;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGradeAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assignmentId = getIntent().getIntExtra("assignmentId", -1);
        assignmentTitle = getIntent().getStringExtra("assignmentTitle");

        submissionDao = AppDatabase.get(this).assignmentSubmissionDao();
        userDao = AppDatabase.get(this).userDao();

        binding.tvAssignmentTitle.setText("Grade: " + assignmentTitle);

        binding.rvSubmissions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubmissionGradingAdapter(this);
        binding.rvSubmissions.setAdapter(adapter);

        binding.btnBack.setOnClickListener(v -> finish());

        loadSubmissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubmissions();
    }

    private void loadSubmissions() {
        AppDatabase.exec.execute(() -> {
            List<AssignmentSubmission> submissions = submissionDao.getSubmissionsByAssignment(assignmentId);
            List<SubmissionGradingAdapter.SubmissionWithStudent> itemsWithStudents = new ArrayList<>();

            for (AssignmentSubmission submission : submissions) {
                User student = userDao.getUserById(submission.getUserId());
                if (student != null && !student.isAdmin()) {
                    itemsWithStudents.add(new SubmissionGradingAdapter.SubmissionWithStudent(student, submission));
                }
            }

            runOnUiThread(() -> adapter.set(itemsWithStudents));
        });
    }

    @Override
    public void onMarkDone(SubmissionGradingAdapter.SubmissionWithStudent item) {
        AppDatabase.exec.execute(() -> {
            item.submission.setSubmitted(true);
            item.submission.setSubmittedDate(System.currentTimeMillis());
            submissionDao.update(item.submission);
            runOnUiThread(() -> {
                Toast.makeText(GradeAssignmentActivity.this, item.student.getUsername() + " marked as done", Toast.LENGTH_SHORT).show();
                loadSubmissions();
            });
        });
    }

    @Override
    public void onGradeStudent(SubmissionGradingAdapter.SubmissionWithStudent item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grade " + item.student.getUsername());

        EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter grade (0-100)");
        if (item.submission.getGrade() >= 0) {
            input.setText(String.valueOf(item.submission.getGrade()));
        }

        builder.setView(input);
        builder.setPositiveButton("Save", (dialog, which) -> {
            try {
                int grade = Integer.parseInt(input.getText().toString());
                if (grade < 0 || grade > 100) {
                    Toast.makeText(this, "Grade must be between 0-100", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppDatabase.exec.execute(() -> {
                    item.submission.setGrade(grade);
                    submissionDao.update(item.submission);
                    runOnUiThread(() -> {
                        Toast.makeText(GradeAssignmentActivity.this, "Grade saved for " + item.student.getUsername(), Toast.LENGTH_SHORT).show();
                        loadSubmissions();
                    });
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
