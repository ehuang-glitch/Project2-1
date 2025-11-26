package com.example.project2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private TextView tvUserMenu;
    private Button btnAdmin;
    private RecyclerView rvCourses;
    private CourseAdapter adapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // 1. Initialize Views
        tvUserMenu = findViewById(R.id.tvUserMenu);
        btnAdmin = findViewById(R.id.btnAdmin);
        rvCourses = findViewById(R.id.rvCourses);

        // 2. Setup RecyclerView Layout Manager
        rvCourses.setLayoutManager(new LinearLayoutManager(this));

        // 3. Populate Data (Ideally this comes from your Room database later)
        List courseList = new ArrayList<>();
        courseList.add(new Course("CS 101", "Intro to Computer Science", "92%", 92, "• Lab 1\n• Quiz 2"));
        courseList.add(new Course("MATH 202", "Calculus II", "85%", 85, "• Problem Set 4\n• Midterm Prep"));
        courseList.add(new Course("HIST 110", "World History", "78%", 78, "• Essay Draft\n• Reading Ch 5"));
        courseList.add(new Course("ENG 101", "Composition I", "95%", 95, "• Final Paper"));

        // 4. Attach Adapter
        adapter = new CourseAdapter(courseList);
        rvCourses.setAdapter(adapter);

        // 5. Existing UI Logic
        tvUserMenu.setText("Test User");

        // Example: Only show admin button if user is admin
        boolean isAdmin = true; // Change this based on actual login logic
        if (isAdmin) {
            btnAdmin.setVisibility(View.VISIBLE);
        } else {
            btnAdmin.setVisibility(View.GONE);
        }

        tvUserMenu.setOnClickListener(this::showUserMenu);
        btnAdmin.setOnClickListener(v ->
                Toast.makeText(this, "Admin Dashboard ", Toast.LENGTH_SHORT).show());
    }

    private void showUserMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_user_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                // Add actual logout logic here (e.g., finish() or return to login screen)
                return true;
            }
            return false;
        });
        popup.show();
    }
}