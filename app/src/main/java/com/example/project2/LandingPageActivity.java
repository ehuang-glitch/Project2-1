package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.entities.Course;

import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_landing_page);

        RecyclerView rv = findViewById(R.id.rvCourses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter();
        rv.setAdapter(adapter);

        // ADMIN BUTTON
        Button btnAdmin = findViewById(R.id.btnAdmin);
        btnAdmin.setVisibility(View.VISIBLE);
        btnAdmin.setOnClickListener(v ->
                startActivity(new Intent(this, AdminDashboardActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume(); // refresh everytime user returns

        AppDatabase db = AppDatabase.get(getApplicationContext());
        CourseDao dao = db.courseDao();

        AppDatabase.exec.execute(() -> {
            List<Course> data = dao.getByUser(1);
            runOnUiThread(() -> adapter.set(data));
        });
    }
}
