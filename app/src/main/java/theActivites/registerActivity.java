package theActivites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database.CanvasCloneDatabase;
import database.CanvasCloneRepository;
import database.UserDAO;
import database.entites.User;
import com.example.canvasclone_3.R;
import com.example.canvasclone_3.databinding.ActivityLoginBinding;
import com.example.canvasclone_3.databinding.ActivityRegisterBinding;

public class registerActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private CanvasCloneRepository repository;

    EditText etUser, etPass;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserDAO dao = CanvasCloneDatabase.getDatabase(getApplicationContext()).userDao();


        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.etRegUsername.getText().toString().trim();
            String password = binding.etRegPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }








            CanvasCloneDatabase.databaseWriteExecutor.execute(() -> {
                User newUser = new User(username, password);
                newUser.setAdmin(false);



                dao.insert(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    static Intent registerIntentFactory(Context context){
        return new Intent(context, registerActivity.class);


    }
}

