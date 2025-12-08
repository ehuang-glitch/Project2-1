package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.util.EdgeToEdgeHelper;

import com.example.project2.databinding.ActivityLoginBinding;
import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle edge-to-edge display
        EdgeToEdgeHelper.applyInsets(binding.getRoot());

        UserDao dao = AppDatabase.get(getApplicationContext()).userDao();

        binding.loginButton.setOnClickListener(v -> {
            String u = binding.userNameLoginEditText.getText().toString().trim();
            String p = binding.passwordLoginEditText.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = dao.getUserByUsername(u);

            if (user == null || !user.getPassword().equals(p)) {
                Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user.isAdmin()) {
                Intent i = new Intent(this, AdminDashboardActivity.class);
                i.putExtra("userId", user.getId());
                startActivity(i);
            } else {
                Intent i = new Intent(this, LandingPageActivity.class);
                i.putExtra("userId", user.getId());
                startActivity(i);
            }

            finish();
        });

        binding.SignUpButton.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
