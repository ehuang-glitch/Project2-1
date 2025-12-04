package com.example.project2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.Course;
import com.example.project2.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Course.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract CourseDao courseDao();

    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService exec =
            Executors.newFixedThreadPool(4);

    public static AppDatabase get(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {

                INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                "app_db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();

                seedDatabase(INSTANCE);
            }
        }
        return INSTANCE;
    }

    private static void seedDatabase(AppDatabase db) {
        if (db.userDao().countUsers() == 0) {

            User u1 = new User("testuser1", "testuser1");
            u1.setAdmin(false);

            User u2 = new User("admin2", "admin2");
            u2.setAdmin(true);

            db.userDao().insert(u1, u2);
        }
    }
}
