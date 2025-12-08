package com.example.project2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project2.util.EdgeToEdgeHelper;

import com.example.project2.databinding.ActivityManageAssignmentsBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentDao;
import com.example.project2.database.entities.Assignment;

import java.util.List;

public class ManageAssignmentsActivity extends AppCompatActivity implements AssignmentManagementAdapter.OnAssignmentActionListener {

    private ActivityManageAssignmentsBinding binding;
    private AssignmentManagementAdapter adapter;
    private int courseId;
    private String courseName;
    private AssignmentDao assignmentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAssignmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeHelper.applyInsets(binding.getRoot());

        // Get course info
        courseId = getIntent().getIntExtra("courseId", -1);
        courseName = getIntent().getStringExtra("courseName");

        // Setup database
        assignmentDao = AppDatabase.get(this).assignmentDao();

        // Setup header
        binding.tvCourseInfo.setText(courseName + " - Assignments");

        // Setup RecyclerView
        binding.rvAssignments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssignmentManagementAdapter(this);
        binding.rvAssignments.setAdapter(adapter);

        // Back button
        binding.btnBack.setOnClickListener(v -> finish());

        // Add assignment button
        binding.btnAddAssignment.setOnClickListener(v -> openAssignmentForm(null));

        loadAssignments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssignments();
    }

    private void loadAssignments() {
        AppDatabase.exec.execute(() -> {
            List<Assignment> assignments = assignmentDao.getAssignmentsByCourse(courseId);
            runOnUiThread(() -> adapter.set(assignments));
        });
    }

    @Override
    public void onEditAssignment(Assignment assignment) {
        openAssignmentForm(assignment);
    }

    @Override
    public void onDeleteAssignment(Assignment assignment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Assignment")
                .setMessage("Are you sure you want to delete this assignment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    AppDatabase.exec.execute(() -> {
                        assignmentDao.delete(assignment);
                        runOnUiThread(() -> {
                            Toast.makeText(ManageAssignmentsActivity.this, "Assignment deleted", Toast.LENGTH_SHORT).show();
                            loadAssignments();
                        });
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onGradeAssignment(Assignment assignment) {
        Intent intent = new Intent(this, GradeAssignmentActivity.class);
        intent.putExtra("assignmentId", assignment.getId());
        intent.putExtra("assignmentTitle", assignment.getTitle());
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    private void openAssignmentForm(Assignment assignment) {
        Intent intent = new Intent(this, AssignmentFormActivity.class);
        intent.putExtra("courseId", courseId);
        if (assignment != null) {
            intent.putExtra("assignmentId", assignment.getId());
            intent.putExtra("title", assignment.getTitle());
            intent.putExtra("description", assignment.getDescription());
            intent.putExtra("dueDate", assignment.getDueDate());
        }
        startActivity(intent);
    }
}
