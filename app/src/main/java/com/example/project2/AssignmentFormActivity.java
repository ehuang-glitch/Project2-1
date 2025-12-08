package com.example.project2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.databinding.ActivityAssignmentFormBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.AssignmentDao;
import com.example.project2.database.dao.AssignmentSubmissionDao;
import com.example.project2.database.dao.CourseUserDao;
import com.example.project2.database.entities.Assignment;
import com.example.project2.database.entities.AssignmentSubmission;
import com.example.project2.database.entities.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssignmentFormActivity extends AppCompatActivity {

    private ActivityAssignmentFormBinding binding;
    private int courseId;
    private int assignmentId = -1;
    private long selectedDueDate = 0;
    private int selectedHour = 0;
    private int selectedMinute = 0;
    private AssignmentDao assignmentDao;
    private AssignmentSubmissionDao submissionDao;
    private CourseUserDao courseUserDao;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        courseId = getIntent().getIntExtra("courseId", -1);
        assignmentId = getIntent().getIntExtra("assignmentId", -1);

        assignmentDao = AppDatabase.get(this).assignmentDao();
        submissionDao = AppDatabase.get(this).assignmentSubmissionDao();
        courseUserDao = AppDatabase.get(this).courseUserDao();

        selectedCalendar = Calendar.getInstance();

        if (assignmentId != -1) {
            // Edit mode
            binding.tvTitle.setText("Edit Assignment");
            binding.btnSaveAssignment.setText("Update Assignment");

            String title = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("description");
            selectedDueDate = getIntent().getLongExtra("dueDate", 0);

            binding.etAssignmentTitle.setText(title);
            binding.etAssignmentDescription.setText(description);
            
            if (selectedDueDate > 0) {
                selectedCalendar.setTimeInMillis(selectedDueDate);
                selectedHour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
                selectedMinute = selectedCalendar.get(Calendar.MINUTE);
                updateDueDateTimeDisplay();
            }
        } else {
            // Add mode - set time to midnight (00:00)
            binding.tvTitle.setText("Add Assignment");
            binding.btnSaveAssignment.setText("Create Assignment");
            selectedHour = 0;
            selectedMinute = 0;
            updateTimeDisplay();
        }

        binding.btnSelectDate.setOnClickListener(v -> showDatePicker());
        binding.btnSelectTime.setOnClickListener(v -> showTimePicker());

        binding.btnSaveAssignment.setOnClickListener(v -> saveAssignment());
        binding.btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDueDate > 0) {
            calendar.setTimeInMillis(selectedDueDate);
        }

        DatePickerDialog dateDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(year, month, dayOfMonth, selectedHour, selectedMinute, 0);
                    selectedDueDate = selectedCalendar.getTimeInMillis();
                    updateDueDateTimeDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dateDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timeDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    
                    // Update the calendar with new time
                    if (selectedDueDate > 0) {
                        selectedCalendar.setTimeInMillis(selectedDueDate);
                    }
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedCalendar.set(Calendar.MINUTE, minute);
                    selectedDueDate = selectedCalendar.getTimeInMillis();
                    
                    updateDueDateTimeDisplay();
                },
                selectedHour,
                selectedMinute,
                true // 24-hour format
        );
        timeDialog.show();
    }

    private void updateDueDateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        binding.tvDueDate.setText(sdf.format(new Date(selectedDueDate)));
        updateTimeDisplay();
    }

    private void updateTimeDisplay() {
        binding.tvDueTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
    }

    private void saveAssignment() {
        String title = binding.etAssignmentTitle.getText().toString().trim();
        String description = binding.etAssignmentDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter assignment title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDueDate == 0) {
            Toast.makeText(this, "Please select a due date", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.exec.execute(() -> {
            if (assignmentId != -1) {
                // Update existing assignment
                Assignment assignment = new Assignment();
                assignment.setId(assignmentId);
                assignment.setCourseId(courseId);
                assignment.setTitle(title);
                assignment.setDescription(description);
                assignment.setDueDate(selectedDueDate);

                assignmentDao.update(assignment);
                runOnUiThread(() -> {
                    Toast.makeText(AssignmentFormActivity.this, "Assignment updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                // Create new assignment
                Assignment assignment = new Assignment(courseId, title, description, selectedDueDate);
                long newAssignmentId = assignmentDao.insert(assignment);

                // Create submission records for all enrolled students
                List<User> enrolledStudents = courseUserDao.getUsersInCourse(courseId);
                for (User student : enrolledStudents) {
                    submissionDao.insert(new AssignmentSubmission((int) newAssignmentId, student.getId()));
                }

                runOnUiThread(() -> {
                    Toast.makeText(AssignmentFormActivity.this, "Assignment created", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}
