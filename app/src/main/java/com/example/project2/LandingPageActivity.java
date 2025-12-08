package com.example.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.databinding.ActivityLandingPageBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentSubmissionDao;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.AssignmentSubmission;
import com.example.project2.database.entities.Course;
import com.example.project2.database.entities.User;

import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private ActivityLandingPageBinding binding;
    private CourseAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get logged-in user ID passed from LoginActivity
        userId = getIntent().getIntExtra("userId", -1);

        // Fetch user information
        UserDao userDao = AppDatabase.get(this).userDao();
        User currentUser = userDao.getUserById(userId);

        // --- Username display ---
        if (currentUser != null) {
            binding.tvUserMenu.setText(currentUser.getUsername());
        }

        // --- Logout Button ---
        binding.btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        // --- Setup RecyclerView for displaying courses ---
        binding.rvCourses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this, userId);
        binding.rvCourses.setAdapter(adapter);

        loadDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        CourseDao courseDao = AppDatabase.get(this).courseDao();
        AssignmentSubmissionDao submissionDao = AppDatabase.get(this).assignmentSubmissionDao();

        AppDatabase.exec.execute(() -> {
            // Get courses enrolled by this user
            List<Course> courseList = courseDao.getCoursesByUser(userId);
            
            // Get all submissions for this user
            List<AssignmentSubmission> submissions = submissionDao.getSubmissionsByUser(userId);

            // Calculate statistics
            final int enrolledCount = courseList.size();
            int submittedCount = 0;
            double totalGrade = 0;
            int gradedCount = 0;

            if (submissions != null) {
                for (AssignmentSubmission submission : submissions) {
                    if (submission.isSubmitted()) {
                        submittedCount++;
                    }
                    if (submission.getGrade() >= 0) {
                        totalGrade += submission.getGrade();
                        gradedCount++;
                    }
                }
            }

            final int finalSubmittedCount = submittedCount;
            final int averageGrade = gradedCount > 0 ? (int) (totalGrade / gradedCount) : 0;

            runOnUiThread(() -> {
                // Update adapter with courses
                adapter.set(courseList);

                // Update stats
                binding.tvEnrolledCount.setText(String.valueOf(enrolledCount));
                binding.tvSubmittedCount.setText(String.valueOf(finalSubmittedCount));
                
                if (averageGrade > 0) {
                    binding.tvAverageGrade.setText(averageGrade + "%");
                } else {
                    binding.tvAverageGrade.setText("--");
                }
            });
        });
    }
}
