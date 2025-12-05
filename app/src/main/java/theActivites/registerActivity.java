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

        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        String username = binding.etRegUsername.getText().toString();
        String password = binding.etRegPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Button btnSignup = findViewById(R.id.btnRegister);

        UserDAO dao = CanvasCloneDatabase.getDatabase(getApplicationContext()).userDao();

        btnSignup.setOnClickListener(v -> {
            User newUser = new User(
                    etUser.getText().toString().trim(),
                    etPass.getText().toString().trim()
            );
            newUser.setAdmin(false);

            dao.insert(newUser);

            Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    static Intent registerIntentFactory(Context context){
        return new Intent(context, registerActivity.class);


    }
}
