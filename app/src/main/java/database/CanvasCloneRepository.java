package database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import database.entites.CanvasClone;
import database.entites.User;
import viewHolder.MainActivity;

public class CanvasCloneRepository {

    private static CanvasCloneRepository repsository;


    private final CanvasCloneDAO canvasCloneDAO;
    private final UserDAO userDAO;
    private ArrayList<CanvasClone> allLogs;

    private CanvasCloneRepository(Application application) {
        CanvasCloneDatabase db = CanvasCloneDatabase.getDatabase(application);
        this.canvasCloneDAO = db.gymLogDAO();
        this.userDAO = db.userDao();
        this.allLogs = (ArrayList<CanvasClone>) this.canvasCloneDAO.getAllRecords();
    }

    public static CanvasCloneRepository getReposoitory(Application application) {
        if (repsository != null) {
            return repsository;
        }
        Future<CanvasCloneRepository> future = CanvasCloneDatabase.databaseWriteExecutor.submit(new Callable<CanvasCloneRepository>() {
                                                                                                    @Override
                                                                                                    public CanvasCloneRepository call() throws Exception {
                                                                                                        return new CanvasCloneRepository(application);
                                                                                                    }
                                                                                                }


        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.tag, "Problem getting GymLogRepository, thread error ");
        }
        return null;
    }

    public ArrayList<CanvasClone> getAllLogs() {
        Future<ArrayList<CanvasClone>> future = CanvasCloneDatabase.databaseWriteExecutor.submit(new Callable<ArrayList<CanvasClone>>() {
            @Override
            public ArrayList<CanvasClone> call() throws Exception {
                return (ArrayList<CanvasClone>) canvasCloneDAO.getAllRecords();
            }
        });
        try {
            return future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i(MainActivity.tag, "problem when getting all GymLogs in repository");
        }
        return null;
    }

    public void insertGymLog(CanvasClone canvasClone) {
        CanvasCloneDatabase.databaseWriteExecutor.execute(() -> {
            canvasCloneDAO.insert(canvasClone);
        });
    }

    public void insertUser(User...user) {
        CanvasCloneDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {

        return userDAO.getUserByUserName(username);

    }

    public LiveData<User> getUserByUserID(int userID) {

        return userDAO.getUserByUserID(userID);

    }

    public LiveData<List<CanvasClone>> updateSharedPreferenceLiveData(int loggedInUserID) {
        return canvasCloneDAO.getAllRecordsByUserIDLiveData(loggedInUserID);
    }


    @Deprecated
    public ArrayList<CanvasClone> getAllLogsByUserID(int loggedInUserID) {

        Future<ArrayList<CanvasClone>> future = CanvasCloneDatabase.databaseWriteExecutor.submit(new Callable<ArrayList<CanvasClone>>() {
            @Override
            public ArrayList<CanvasClone> call() throws Exception {
                return (ArrayList<CanvasClone>) canvasCloneDAO.getAllRecordsByUserID(loggedInUserID);
            }
        });
        try {
            return future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i(MainActivity.tag, "problem when getting all GymLogs in repository");
        }
        return null;

    }
}
