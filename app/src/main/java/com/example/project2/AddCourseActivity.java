package com.example.project2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.util.EdgeToEdgeHelper;

import com.example.project2.databinding.ActivityAddCourseBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.dao.CourseUserDao;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.Course;
import com.example.project2.database.entities.CourseUser;
import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {

    private ActivityAddCourseBinding binding;
    private StudentCheckboxAdapter studentAdapter;
    private List<StudentCheckboxAdapter.StudentCheckItem> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeHelper.applyInsets(binding.getRoot());

        // BACK BUTTON
        binding.btnBack.setOnClickListener(v -> finish());

        // Setup student selection RecyclerView
        binding.rvSelectedStudents.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentCheckboxAdapter();
        binding.rvSelectedStudents.setAdapter(studentAdapter);
        studentList = new ArrayList<>();

        // Load available students
        loadStudents();

        // Open student selection dialog
        binding.btnSelectStudents.setOnClickListener(v -> showStudentDialog());

        // ADD BUTTON
        CourseDao courseDao = AppDatabase.get(getApplicationContext()).courseDao();
        CourseUserDao courseUserDao = AppDatabase.get(getApplicationContext()).courseUserDao();

        binding.btnAddCourse.setOnClickListener(v -> {
            String code = binding.etCode.getText().toString().trim();
            String name = binding.etName.getText().toString().trim();

            if (code.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Course c = new Course(code, name);

            AppDatabase.exec.execute(() -> {
                long courseId = courseDao.insert(c);

                // Enroll selected students
                List<Integer> selectedUserIds = studentAdapter.getSelectedUserIds();
                for (int userId : selectedUserIds) {
                    courseUserDao.insert(new CourseUser((int)courseId, userId));
                }

                runOnUiThread(() -> {
                    Toast.makeText(AddCourseActivity.this, "Course added!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    private void loadStudents() {
        UserDao userDao = AppDatabase.get(this).userDao();

        AppDatabase.exec.execute(() -> {
            List<User> users = userDao.getAll();
            studentList.clear();

            for (User user : users) {
                if (!user.isAdmin()) {
                    studentList.add(new StudentCheckboxAdapter.StudentCheckItem(user, false));
                }
            }

            runOnUiThread(() -> studentAdapter.set(studentList));
        });
    }

    private void showStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Students");
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
