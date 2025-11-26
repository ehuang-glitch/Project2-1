package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.entities.Course;

import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements AdminCourseAdapter.OnCourseClickListener {

    AdminCourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        RecyclerView rv = findViewById(R.id.rvAdminCourses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminCourseAdapter(this);
        rv.setAdapter(adapter);

        // Add Course button (in header)
        TextView btnAddCourse = findViewById(R.id.btnAddCourseHeader);
        btnAddCourse.setOnClickListener(v ->
                startActivity(new Intent(this, AddCourseActivity.class)));

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
        i.putExtra("percentage", c.getPercentage());
        i.putExtra("assignments", c.getAssignments());
        i.putExtra("userId", c.getUserId());
        startActivity(i);
    }
}
