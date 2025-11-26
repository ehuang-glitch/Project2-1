package com.example.project2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.entities.Course;

public class EditCourseActivity extends AppCompatActivity {

    EditText etCode, etName, etScore, etAssignments, etUserId;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);
        etScore = findViewById(R.id.etScore);
        etAssignments = findViewById(R.id.etAssignments);
        etUserId = findViewById(R.id.etUserId);

        Button btnSave = findViewById(R.id.btnSaveCourse);

        courseId = getIntent().getIntExtra("id", -1);
        etCode.setText(getIntent().getStringExtra("code"));
        etName.setText(getIntent().getStringExtra("name"));
        etScore.setText(String.valueOf(getIntent().getIntExtra("percentage", 0)));
        etAssignments.setText(getIntent().getStringExtra("assignments"));
        etUserId.setText(String.valueOf(getIntent().getIntExtra("userId", 1)));

        CourseDao dao = AppDatabase.get(getApplicationContext()).courseDao();

        btnSave.setOnClickListener(v -> {
            Course c = new Course(
                    etCode.getText().toString().trim(),
                    etName.getText().toString().trim(),
                    Integer.parseInt(etScore.getText().toString().trim()),
                    etAssignments.getText().toString().trim(),
                    Integer.parseInt(etUserId.getText().toString().trim())
            );

            c.setId(courseId);

            AppDatabase.exec.execute(() -> {
                dao.insert(c);
                finish();
            });
        });
    }
}
