package com.example.project2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.entities.Course;

public class AddCourseActivity extends AppCompatActivity {

    EditText etCode, etName, etScore, etAssignments, etUserId;
    CourseDao dao;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_course);

        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);
        etScore = findViewById(R.id.etScore);
        etAssignments = findViewById(R.id.etAssignments);
        etUserId = findViewById(R.id.etUserId);
        Button btnAdd = findViewById(R.id.btnAddCourse);

        dao = AppDatabase.get(getApplicationContext()).courseDao();

        btnAdd.setOnClickListener(v -> {
            Course c = new Course(
                    etCode.getText().toString(),
                    etName.getText().toString(),
                    Integer.parseInt(etScore.getText().toString()),
                    etAssignments.getText().toString(),
                    Integer.parseInt(etUserId.getText().toString())
            );

            AppDatabase.exec.execute(() -> {
                dao.insert(c);
                finish();
            });
        });
    }
}
