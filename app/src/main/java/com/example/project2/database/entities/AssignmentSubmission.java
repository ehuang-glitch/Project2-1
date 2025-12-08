package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "assignment_submissions",
    indices = {
        @Index("assignmentId"),
        @Index("userId"),
        @Index(value = {"assignmentId", "userId"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey(
            entity = Assignment.class,
            parentColumns = "id",
            childColumns = "assignmentId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "userId",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class AssignmentSubmission {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int assignmentId;
    private int userId;
    private boolean submitted;
    private int grade;
    private long submittedDate;

    public AssignmentSubmission() {
        this.submitted = false;
        this.grade = -1; // -1 means not graded
        this.submittedDate = 0;
    }

    @Ignore
    public AssignmentSubmission(int assignmentId, int userId) {
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.submitted = false;
        this.grade = -1;
        this.submittedDate = 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(long submittedDate) {
        this.submittedDate = submittedDate;
    }
}
