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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // BACK BUTTON
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // FORM FIELDS
        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);
        etScore = findViewById(R.id.etScore);
        etAssignments = findViewById(R.id.etAssignments);
        etUserId = findViewById(R.id.etUserId);

        // ADD BUTTON
        Button btnAdd = findViewById(R.id.btnAddCourse);

        CourseDao dao = AppDatabase.get(getApplicationContext()).courseDao();

        btnAdd.setOnClickListener(v -> {

            Course c = new Course(
                    etCode.getText().toString().trim(),
                    etName.getText().toString().trim(),
                    Integer.parseInt(etScore.getText().toString().trim()),
                    etAssignments.getText().toString().trim(),
                    Integer.parseInt(etUserId.getText().toString().trim())
            );

            AppDatabase.exec.execute(() -> {
                dao.insert(c);
                finish();
            });
        });
    }
}
