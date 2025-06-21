package com.example.cloudcounselage;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private TextView welcomeTextView;
    private Button chatbotButton, logoutButton;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        setupFirebase();
        setupToolbar();
        setupUserInfo();
        setupClickListeners();
    }

    private void initializeViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        chatbotButton = findViewById(R.id.chatbotButton);
        logoutButton = findViewById(R.id.logoutButton);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }

    private void setupUserInfo() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                String username = email.split("@")[0];
                welcomeTextView.setText("Welcome, " + username + "!");
            }
        }
    }

    private void setupClickListeners() {
        chatbotButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ChatbotActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to login screen
        super.onBackPressed();
        moveTaskToBack(true);
    }
}