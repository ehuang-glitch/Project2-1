package com.example.project2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.databinding.ActivityEditCourseBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.dao.CourseUserDao;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.Course;
import com.example.project2.database.entities.CourseUser;
import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {

    private ActivityEditCourseBinding binding;
    private int courseId;
    private String courseName;
    private StudentCheckboxAdapter studentAdapter;
    private List<StudentCheckboxAdapter.StudentCheckItem> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getIntExtra("id", -1);
        courseName = getIntent().getStringExtra("name");
        binding.etCode.setText(getIntent().getStringExtra("code"));
        binding.etName.setText(courseName);

        // Setup student selection RecyclerView
        binding.rvSelectedStudents.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentCheckboxAdapter();
        binding.rvSelectedStudents.setAdapter(studentAdapter);
        studentList = new ArrayList<>();

        // Load available students
        loadStudents();

        // Open student selection dialog
        binding.btnSelectStudents.setOnClickListener(v -> showStudentDialog());

        // Manage Assignments button
        binding.btnManageAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageAssignmentsActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("courseName", courseName);
            startActivity(intent);
        });

        CourseDao courseDao = AppDatabase.get(getApplicationContext()).courseDao();
        CourseUserDao courseUserDao = AppDatabase.get(getApplicationContext()).courseUserDao();

        // Save/Update button
        binding.btnSaveCourse.setOnClickListener(v -> {
            if (binding.etCode.getText().toString().trim().isEmpty() || 
                binding.etName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Course c = new Course(
                    binding.etCode.getText().toString().trim(),
                    binding.etName.getText().toString().trim()
            );

            c.setId(courseId);

            AppDatabase.exec.execute(() -> {
                courseDao.update(c);

                // Update enrolled students
                courseUserDao.deleteAllUsersFromCourse(courseId);
                List<Integer> selectedUserIds = studentAdapter.getSelectedUserIds();
                for (int userId : selectedUserIds) {
                    courseUserDao.insert(new CourseUser(courseId, userId));
                }

                runOnUiThread(() -> {
                    Toast.makeText(EditCourseActivity.this, "Course updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });

        // Delete button
        binding.btnDeleteCourse.setOnClickListener(v -> {
            Course c = new Course();
            c.setId(courseId);

            AppDatabase.exec.execute(() -> {
                courseDao.delete(c);
                runOnUiThread(() -> {
                    Toast.makeText(EditCourseActivity.this, "Course deleted!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    private void loadStudents() {
        UserDao userDao = AppDatabase.get(this).userDao();
        CourseUserDao courseUserDao = AppDatabase.get(this).courseUserDao();

        AppDatabase.exec.execute(() -> {
            List<User> users = userDao.getAll();
            List<User> enrolledUsers = courseUserDao.getUsersInCourse(courseId);

            studentList.clear();

            for (User user : users) {
                if (!user.isAdmin()) {
                    boolean isEnrolled = false;
                    for (User enrolled : enrolledUsers) {
                        if (enrolled.getId() == user.getId()) {
                            isEnrolled = true;
                            break;
                        }
                    }
                    studentList.add(new StudentCheckboxAdapter.StudentCheckItem(user, isEnrolled));
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
