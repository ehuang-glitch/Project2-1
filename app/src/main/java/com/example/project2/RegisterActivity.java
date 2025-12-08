package com.example.project2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.util.EdgeToEdgeHelper;

import com.example.project2.databinding.ActivityRegisterBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeHelper.applyInsets(binding.getRoot());

        UserDao dao = AppDatabase.get(getApplicationContext()).userDao();

        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.etRegUsername.getText().toString().trim();
            String password = binding.etRegPassword.getText().toString().trim();

            // Validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() < 3) {
                Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 3) {
                Toast.makeText(this, "Password must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.exec.execute(() -> {
                // Check if username already exists
                User existingUser = dao.getUserByUsername(username);
                if (existingUser != null) {
                    runOnUiThread(() -> 
                        Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                User newUser = new User(username, password);
                newUser.setAdmin(false);

                dao.insert(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
