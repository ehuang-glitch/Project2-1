package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.Course;
import com.example.project2.database.entities.User;

import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private CourseAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Get logged-in user ID passed from LoginActivity
        userId = getIntent().getIntExtra("userId", -1);

        // Fetch user information
        UserDao userDao = AppDatabase.get(this).userDao();
        User currentUser = userDao.getUserById(userId);

        // --- Username display ---
        TextView tvUserMenu = findViewById(R.id.tvUserMenu);
        tvUserMenu.setText(currentUser.getUsername());

        // --- Logout Button ---
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        // --- Setup RecyclerView for displaying courses ---
        RecyclerView rv = findViewById(R.id.rvCourses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter();
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load user-specific courses on resume
        CourseDao courseDao = AppDatabase.get(this).courseDao();

        AppDatabase.exec.execute(() -> {
            List<Course> courseList = courseDao.getByUser(userId);

            runOnUiThread(() -> adapter.set(courseList));
        });
    }
}
