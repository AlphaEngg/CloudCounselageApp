package com.example.cloudcounselage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cloudcounselage.utils.ValidationUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;

import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private EditText usernameEditText, emailEditText, passwordEditText, retypePasswordEditText;
    private Button signUpButton, googleSignUpButton, facebookSignUpButton;
    private TextView loginTextView;

    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseApp.initializeApp(this);

        initializeViews();
        setupFirebase();
        setupGoogleSignIn();
        setupFacebookSignIn();
        setupClickListeners();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        retypePasswordEditText = findViewById(R.id.retypePasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);
        facebookSignUpButton = findViewById(R.id.facebookSignUpButton);
        loginTextView = findViewById(R.id.loginTextView);
    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupFacebookSignIn() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        // Correct provider
                        handleFacebookAccessToken(result.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignUpActivity.this, "Facebook signup cancelled.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException ex) {
                        Toast.makeText(SignUpActivity.this, "FB signup error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupClickListeners() {
        signUpButton.setOnClickListener(v -> performEmailSignUp());
        googleSignUpButton.setOnClickListener(v -> signInWithGoogle());
        facebookSignUpButton.setOnClickListener(v -> signInWithFacebook());
        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void performEmailSignUp() {
        String username = usernameEditText.getText().toString().trim();
        String email    = emailEditText.getText().toString().trim();
        String pwd      = passwordEditText.getText().toString().trim();
        String rePwd    = retypePasswordEditText.getText().toString().trim();

        if (!ValidationUtils.isNotEmpty(username)) {
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show(); return;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show(); return;
        }
        if (!ValidationUtils.doPasswordsMatch(pwd, rePwd)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show(); return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else {
                        Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Google login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Google authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential facebookCredential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(facebookCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Toast.makeText(SignUpActivity.this, "Facebook login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthUserCollisionException) {

                            FirebaseAuthUserCollisionException ex = (FirebaseAuthUserCollisionException) e;


                            AuthCredential pendingFacebook = facebookCredential;


                            String email = ex.getEmail();
                            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                                    .addOnSuccessListener(result -> {
                                        if (result.getSignInMethods().contains(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {

                                            Toast.makeText(SignUpActivity.this, "Please log in with Google to link accounts", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Account exists with different provider.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUpActivity.this, "Facebook login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void handleSocialAuth(AuthCredential cred) {
        firebaseAuth.signInWithCredential(cred)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Social login OK", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else {
                        Toast.makeText(this, "Social auth failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToHome() {
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        finish();
    }
}
