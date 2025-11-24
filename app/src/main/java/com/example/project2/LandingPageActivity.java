package com.example.project2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LandingPageActivity extends AppCompatActivity {

    private TextView tvUserMenu;
    private Button btnAdmin;
    private RecyclerView rvCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        tvUserMenu = findViewById(R.id.tvUserMenu);
        btnAdmin = findViewById(R.id.btnAdmin);
        rvCourses = findViewById(R.id.rvCourses);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));

        // Dummy UI setup
        tvUserMenu.setText("Test User");
        btnAdmin.setVisibility(View.VISIBLE);

        tvUserMenu.setOnClickListener(this::showUserMenu);
        btnAdmin.setOnClickListener(v ->
                Toast.makeText(this, "Admin button pressed", Toast.LENGTH_SHORT).show());
    }

    private void showUserMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_user_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popup.show();
    }
}
