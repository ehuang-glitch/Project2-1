package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "course_users",
    indices = {
        @Index("courseId"),
        @Index("userId"),
        @Index(value = {"courseId", "userId"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey(
            entity = Course.class,
            parentColumns = "id",
            childColumns = "courseId",
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
public class CourseUser {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int courseId;
    private int userId;

    public CourseUser() {}

    public CourseUser(int courseId, int userId) {
        this.courseId = courseId;
        this.userId = userId;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
