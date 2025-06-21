package com.example.cloudcounselage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        initializeViews();
        setupRecyclerView();
        setupToolbar();
        setupClickListeners();
        addWelcomeMessage();
    }

    private void initializeViews() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupRecyclerView() {
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void addWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage("Hello! I'm here to help you with Cloud Counselage FAQs. Try asking:\n• What is Cloud Counselage?\n• How to join internship?\n• What services do you offer?", false);
        chatMessages.add(welcomeMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();
        if (message.isEmpty()) return;

        // Add user message
        ChatMessage userMessage = new ChatMessage(message, true);
        chatMessages.add(userMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        // Generate bot response
        String botResponse = generateBotResponse(message.toLowerCase());
        ChatMessage botMessage = new ChatMessage(botResponse, false);
        chatMessages.add(botMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        messageEditText.setText("");
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    private String generateBotResponse(String userInput) {
        userInput = userInput.trim().toLowerCase();

        if (userInput.contains("what is cloud counselage") || userInput.contains("about cloud counselage")) {
            return "Cloud Counselage is an award-winning IT & Management Consulting Company with a social cause to solve unemployability. We're led by founders who have delivered projects across 120 countries and are building a global ecosystem of 100MN+ Professionals across 100+ countries.";
        }

        if (userInput.contains("internship") || userInput.contains("join") || userInput.contains("how to join")) {
            return "To join our internship program, you can visit our website or contact us directly. We offer industry exposure and work experience to make you job-ready at no cost through our 'Industry Academia Community'.";
        }

        if (userInput.contains("services") || userInput.contains("what do you offer")) {
            return "Our services include:\n• Consulting - Cost-effective business management solutions\n• Branding - Purpose-driven branding strategies\n• Hiring - Talent acquisition and retention\n• Learning - Leadership development programs";
        }

        if (userInput.contains("founders") || userInput.contains("who founded")) {
            return "Cloud Counselage is founded by:\n• Tushar Topale (Founder & CVO) - IT professional with 25+ global certifications\n• Harshada Topale (Co-Founder & Director) - Award-winning management professional\n• Subhi Shildhankar (Co-Founder & Director HR) - MBA with Gold Medal";
        }

        if (userInput.contains("vision") || userInput.contains("mission") || userInput.contains("2030")) {
            return "Our Vision 2030: Building a global ecosystem to bring 85 Million global jobs to India by 2030, thereby bringing billion-dollar businesses to India. We aim to eradicate perpetual unemployability among youth.";
        }

        if (userInput.contains("contact") || userInput.contains("office") || userInput.contains("address")) {
            return "Our offices:\nCorporate: 91 Springboard, Vikhroli West, Mumbai - 400079\nRegistered: 91 Springboard, BKC, Mumbai - 400098\n\nVisit our website for more contact details.";
        }

        if (userInput.contains("hello") || userInput.contains("hi") || userInput.contains("hey")) {
            return "Hello! How can I help you learn more about Cloud Counselage today?";
        }

        if (userInput.contains("thank") || userInput.contains("thanks")) {
            return "You're welcome! Feel free to ask any other questions about Cloud Counselage.";
        }

        return "I'm sorry, I didn't understand that. Please try asking about:\n• What is Cloud Counselage?\n• Services we offer\n• How to join internship\n• Our founders\n• Vision 2030\n• Contact information";
    }

    // Inner classes for Chat functionality
    public static class ChatMessage {
        private String message;
        private boolean isUser;

        public ChatMessage(String message, boolean isUser) {
            this.message = message;
            this.isUser = isUser;
        }

        public String getMessage() { return message; }
        public boolean isUser() { return isUser; }
    }
}