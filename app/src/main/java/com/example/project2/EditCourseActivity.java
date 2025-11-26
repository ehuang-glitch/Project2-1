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
    CourseDao dao;
    int id;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_edit_course);

        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);
        etScore = findViewById(R.id.etScore);
        etAssignments = findViewById(R.id.etAssignments);
        etUserId = findViewById(R.id.etUserId);
        Button btnSave = findViewById(R.id.btnSaveCourse);

        dao = AppDatabase.get(getApplicationContext()).courseDao();

        id = getIntent().getIntExtra("id", -1);
        etCode.setText(getIntent().getStringExtra("code"));
        etName.setText(getIntent().getStringExtra("name"));
        etScore.setText(String.valueOf(getIntent().getIntExtra("percentage", 0)));
        etAssignments.setText(getIntent().getStringExtra("assignments"));
        etUserId.setText(String.valueOf(getIntent().getIntExtra("userId", 1)));

        btnSave.setOnClickListener(v -> {

            Course c = new Course(
                    etCode.getText().toString(),
                    etName.getText().toString(),
                    Integer.parseInt(etScore.getText().toString()),
                    etAssignments.getText().toString(),
                    Integer.parseInt(etUserId.getText().toString())
            );

            c.setId(id);

            AppDatabase.exec.execute(() -> {
                dao.insert(c);
                finish();
            });
        });
    }
}
