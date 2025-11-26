package theActivites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.canvasclone_3.R;
import com.example.canvasclone_3.databinding.ActivityMainBinding;

import database.CanvasCloneRepository;
import database.entites.User;

public class MainActivity extends AppCompatActivity {


    public static final String tag = "CCLOG";

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.CanvasClone_3.MAIN_ACTIVITY_USER_ID";
    static final String SHARED_PREFRENCE_USERID_KEY = "com.example.CanvasClone_3.PREFRENCE_USERID_KEY";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.CanvasClone_3.SAVED_INSTANCE_STATE_USERID_KEY";

    private static final int LOGGED_OUT = -1;

    private int loggedInUserID = -1;
    private ActivityMainBinding binding;

    private User user;
    private CanvasCloneRepository repository;

    Button b1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
               // Toast.makeText(MainActivity.this,"LOGOUT TO BE IMPLEMENTED", Toast.LENGTH_SHORT).show();

                showLogoutDialog();

                return false;
            }
        });

        item.setTitle(user != null ? user.getUsername() : "Account");
        return true;
    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        repository = CanvasCloneRepository.getReposoitory(getApplication());
        loginUser(savedInstanceState);





        //User not logged in at this point
        if(loggedInUserID == LOGGED_OUT){
            Intent intent = loginActivity.loginIntentFactory(this);
            startActivity(intent);

        }

        updateSharedPreference();


    }


    private void loginUser(Bundle savedInstanceState) {
        //TODO: create login method
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_user_ID_key),LOGGED_OUT);



        if(loggedInUserID == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserID = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY,LOGGED_OUT);

        }

        if(loggedInUserID == LOGGED_OUT){
            loggedInUserID = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);

        }
        //Checked preference for loggedUser

        if(loggedInUserID == LOGGED_OUT){
            return;
        }

        //Checked intent for loggedUser

        LiveData<User> userObserver = repository.getUserByUserID(loggedInUserID);
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null){
                invalidateOptionsMenu();

            }


            if(user.isAdmin() == true){
                System.out.println("works");
                b1 = (Button) findViewById(R.id.adminButton);
                b1.setVisibility(View.VISIBLE);
            }


        });


    }

    private void showLogoutDialog(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertBuilder.create().show();

    }



    private void logout() {


        loggedInUserID = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);
        startActivity(loginActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedprefernceEditor = sharedPreferences.edit();
        sharedprefernceEditor.putInt(getString(R.string.preference_user_ID_key),loggedInUserID);
        sharedprefernceEditor.apply();


    }

    static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;

    }
}