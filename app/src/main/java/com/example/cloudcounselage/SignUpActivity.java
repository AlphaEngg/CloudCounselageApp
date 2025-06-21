package com.example.cloudcounselage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cloudcounselage.utils.ValidationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, retypePasswordEditText;
    private Button signUpButton;
    private TextView loginTextView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setupFirebase();
        setupClickListeners();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        retypePasswordEditText = findViewById(R.id.retypePasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginTextView = findViewById(R.id.loginTextView);
    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    private void setupClickListeners() {
        signUpButton.setOnClickListener(v -> performSignUp());
        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void performSignUp() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String retypePassword = retypePasswordEditText.getText().toString().trim();

        if (!ValidationUtils.isNotEmpty(username)) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isNotEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Please enter valid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isNotEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.isNotEmpty(retypePassword)) {
            Toast.makeText(this, "Please retype password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidationUtils.doPasswordsMatch(password, retypePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        saveUserToDatabase(userId, username, email);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String userId, String username, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("uid", userId);

        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}