package com.example.project2.database;

import android.app.Application;
import android.util.Log;

import com.example.project2.Course; // Make sure this matches where your Course.java is
import com.example.project2.database.entities.User; // Assumes you moved User to a 'entities' folder, otherwise import appropriately

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CourseRepository {
    // References to the Data Access Objects (DAOs)
    private final CourseDAO courseDAO;
    private final UserDAO userDAO;

    // Singleton instance
    private static CourseRepository repository;

    // Constructor (Private to enforce Singleton)
    private CourseRepository(Application application) {
        CourseDatabase db = CourseDatabase.getDatabase(application);
        this.courseDAO = db.courseDAO();
        this.userDAO = db.userDAO();
    }

    // Public method to get the Repository instance
    public static CourseRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<CourseRepository> future = CourseDatabase.databaseWriteExecutor.submit(
                new Callable<CourseRepository>() {
                    @Override
                    public CourseRepository call() throws Exception {
                        return new CourseRepository(application);
                    }
                }
        );
        try {
            repository = future.get();
            return repository;
        } catch (InterruptedException | ExecutionException e) {
            Log.d("CourseRepository", "Problem getting CourseRepository, thread error");
        }
        return null;
    }

    // Method to get all courses (or specific courses by User ID)
    public List<Course> getCoursesByUserId(int userId) {
        Future<List<Course>> future = CourseDatabase.databaseWriteExecutor.submit(
                new Callable<List<Course>>() {
                    @Override
                    public List<Course> call() throws Exception {
                        return courseDAO.getCoursesByUserId(userId);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i("CourseRepository", "Problem when getting courses from repository");
        }
        return new ArrayList<>(); // Return empty list on failure
    }

    // Method to insert a new course
    public void insertCourse(Course course) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            courseDAO.insert(course);
        });
    }

    // Method to insert a user
    public void insertUser(User... user) {
        CourseDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }
}