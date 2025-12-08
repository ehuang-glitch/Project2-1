package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "assignments",
    indices = {
        @Index("courseId")
    },
    foreignKeys = {
        @ForeignKey(
            entity = Course.class,
            parentColumns = "id",
            childColumns = "courseId",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Assignment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int courseId;
    private String title;
    private String description;
    private long dueDate;

    public Assignment() {}

    @Ignore
    public Assignment(int courseId, String title, String description, long dueDate) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
}
