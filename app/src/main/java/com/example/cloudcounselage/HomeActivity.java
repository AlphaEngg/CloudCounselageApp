package com.example.cloudcounselage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    private LinearLayout chatbotButton;
    private WebView videoWebView;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private final String youtubeVideoId = "JyqAtgKC4I4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        setupFirebase();
        setupToolbar();
        setupUserInfo();
        setupVideoPlayer();
        setupClickListeners();
    }

    private void initializeViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        chatbotButton = findViewById(R.id.chatbotButton);
        videoWebView = findViewById(R.id.videoWebView);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupUserInfo() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            String email = user.getEmail();

            if (displayName != null && !displayName.trim().isEmpty()) {
                welcomeTextView.setText("Welcome, " + displayName + "!");
            } else if (email != null) {
                String username = email.split("@")[0];
                // Capitalize first letter and make it more readable
                username = username.substring(0, 1).toUpperCase() + username.substring(1);
                welcomeTextView.setText("Welcome, " + username + "!");
            } else {
                welcomeTextView.setText("Welcome!");
            }
        }
    }

    private void setupVideoPlayer() {
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        videoWebView.setWebChromeClient(new WebChromeClient()); // Enables fullscreen
        videoWebView.setWebViewClient(new WebViewClient() {
            // Fallback to YouTube app if page fails to load
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                openInYouTubeApp();
            }
        });


        String videoUrl = "https://www.youtube.com/embed/" + youtubeVideoId + "?enablejsapi=1&autoplay=0&rel=0";
        String html = "<html><body style='margin:0;padding:0;'>" +
                "<iframe width='100%' height='100%' " +
                "src='" + videoUrl + "' " +
                "frameborder='0' allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; fullscreen' allowfullscreen>" +
                "</iframe></body></html>";

        videoWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    private void openInYouTubeApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeVideoId));
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open YouTube app", Toast.LENGTH_SHORT).show();
        }
    }


    private void setupClickListeners() {
        // Chatbot Button - Updated to work with LinearLayout
        chatbotButton.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Chat Support...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, ChatbotActivity.class));
        });
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}