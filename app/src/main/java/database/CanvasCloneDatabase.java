package database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.entites.CanvasClone;
import database.entites.User;
import database.typeConverter.LocalDateTypeConverter;
import theActivites.MainActivity;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {CanvasClone.class, User.class}, version = 1 , exportSchema = false)
public abstract class CanvasCloneDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "CanvasCloneDatabase";

    public static final String GYM_LOG_TABLE = "CanvasCloneTable";

    private static volatile CanvasCloneDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static CanvasCloneDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (CanvasCloneDatabase.class){
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CanvasCloneDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().addCallback(addDefaultValues).build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback addDefaultValues = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.tag, "Database created");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDao();
                // dao.deleteALL();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testUser1", "testUser1");
                testUser1.setAdmin(false);
                dao.insert(testUser1);
            });


        }

    };

    //If wrong watch vid 3
    public abstract CanvasCloneDAO gymLogDAO() ;

    public abstract UserDAO userDao();

}
