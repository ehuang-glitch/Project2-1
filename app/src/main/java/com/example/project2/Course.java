package com.example.project2;

public class Course {
    private String courseCode;
    private String courseName;
    private String gradeString;
    private int gradeProgress;
    private String assignments;

    public Course(String courseCode, String courseName, String gradeString, int gradeProgress, String assignments) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.gradeString = gradeString;
        this.gradeProgress = gradeProgress;
        this.assignments = assignments;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getGradeString() { return gradeString; }
    public int getGradeProgress() { return gradeProgress; }
    public String getAssignments() { return assignments; }
}