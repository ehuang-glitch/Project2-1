package com.example.project2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.AppDatabase;
import com.example.project2.database.dao.UserDao;
import com.example.project2.database.entities.User;

public class RegisterActivity extends AppCompatActivity {

    EditText etUser, etPass;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        Button btnSignup = findViewById(R.id.btnRegister);

        UserDao dao = AppDatabase.get(getApplicationContext()).userDao();

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
}
