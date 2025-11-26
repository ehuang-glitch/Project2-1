package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.User;

public class LoginActivity extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.userNameLoginEditText);
        etPass = findViewById(R.id.passwordLoginEditText);
        btnLogin = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.SignUpButton);

        UserDao dao = AppDatabase.get(getApplicationContext()).userDao();

        btnLogin.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();

            User user = dao.getUserByUsername(u);

            if (user == null || !user.getPassword().equals(p)) {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show();
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

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
