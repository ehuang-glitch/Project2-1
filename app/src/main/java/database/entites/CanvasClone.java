package database.entites;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Objects;




    @Entity(tableName = "CanvasCloneTable")
    public class CanvasClone {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private String assignment;
        private int maxGrade;
        private int grade;
        private LocalDateTime date;

        private int userID;

        @NonNull
        @Override
        public String toString() {
            return assignment + '\n' +
                    ", weight= " + maxGrade + '\n' +
                    ", reps= " + grade + '\n' +
                    ", date= " + date.toString() + '\n' +
                    '}';
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public CanvasClone(String exercise, int weight, int reps, int userID) {
            this.assignment = exercise;
            this.maxGrade = weight;
            this.grade = reps;
            date = LocalDateTime.now();
            this.userID = userID;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CanvasClone canvasClone = (CanvasClone) o;
            return id == canvasClone.id && Double.compare(maxGrade, canvasClone.maxGrade) == 0 && grade == canvasClone.grade && userID == canvasClone.userID && Objects.equals(assignment, canvasClone.assignment) && Objects.equals(date, canvasClone.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, assignment, maxGrade, grade, date, userID);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAssignment() {
            return assignment;
        }

        public void setAssignment(String assignment) {
            this.assignment = assignment;
        }

        public int getMaxGrade() {
            return maxGrade;
        }

        public void setMaxGrade(int maxGrade) {
            this.maxGrade = maxGrade;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }
    }


