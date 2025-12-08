package com.example.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.databinding.ActivityAdminDashboardBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.entities.Course;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements AdminCourseAdapter.OnCourseClickListener {

    private ActivityAdminDashboardBinding binding;
    private AdminCourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvAdminCourses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminCourseAdapter(this);
        binding.rvAdminCourses.setAdapter(adapter);

        // Add Course button (in header)
        binding.btnAddCourseHeader.setOnClickListener(v ->
                startActivity(new Intent(this, AddCourseActivity.class)));

        // Logout button
        binding.btnLogout.setOnClickListener(v -> logout());

        loadCourses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }

    private void loadCourses() {
        CourseDao dao = AppDatabase.get(this).courseDao();

        AppDatabase.exec.execute(() -> {
            List<Course> list = dao.getAllCourses();
            runOnUiThread(() -> adapter.set(list));
        });
    }

    @Override
    public void onCourseClick(Course c) {
        Intent i = new Intent(this, EditCourseActivity.class);
        i.putExtra("id", c.getId());
        i.putExtra("code", c.getCode());
        i.putExtra("name", c.getName());
        startActivity(i);
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
