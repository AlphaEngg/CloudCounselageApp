// ChatbotActivity.java (Final Version with More Info & FAQs)
package com.example.cloudcounselage;

import android.os.Bundle;
import android.text.Html;
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
        String welcome = "Welcome to Cloud Counselage Chatbot!<br>" +
                "You can ask about:<br>" +
                "• Company Info<br>" +
                "• Internship<br>" +
                "• Services<br>" +
                "• Founders<br>" +
                "• Vision 2030<br>" +
                "• Contact & Social Media<br>" +
                "• Achievements<br>" +
                "• NGO Initiatives";
        chatMessages.add(new ChatMessage(welcome, false));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();
        if (message.isEmpty()) return;

        chatMessages.add(new ChatMessage(message, true));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        String response = generateBotResponse(message);
        chatMessages.add(new ChatMessage(response, false));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        messageEditText.setText("");
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    private String generateBotResponse(String input) {
        input = input.toLowerCase();

        if (input.contains("what is cloud counselage") || input.contains("about cloud counselage") || input.contains("company info")) {
            return "Cloud Counselage is an award-winning IT & Management Consulting Company with a mission to eradicate unemployability. We operate in over 100 countries, working towards building a global ecosystem of 100MN+ Professionals, 1MN+ Companies, and 1 Lakh+ Colleges by 2030.";

        } else if (input.contains("internship duration") || input.contains("certificate") || input.contains("remote internship")) {
            return "Internship FAQs:<br>• Duration: 6 weeks to 6 months<br>• Mode: Online/Flexi-time<br>• Certificate: Provided upon completion<br>• Domains: 50+ including IT, Mgmt., Humanities<br>• Live Projects: Included<br>• No Fees or Selection Criteria<br>• Apply via: <a href=\"https://www.cloudcounselage.com/\">Website</a><br>• How to apply video: <a href=\"https://www.youtube.com/watch?v=03OiMD--1yo\">Watch on YouTube</a>";

        } else if (input.contains("internship") || input.contains("join") || input.contains("training")) {
            return "We offer free online internships under 'Industry Academia Community'.<br>• Real-time industry exposure<br>• Career mentoring and guidance<br>• Apply via: <a href=\"https://www.cloudcounselage.com/\">Website</a><br>• Application video: <a href=\"https://www.youtube.com/watch?v=03OiMD--1yo\">YouTube Video</a>";

        } else if (input.contains("services") || input.contains("offerings") || input.contains("solutions")) {
            return "We offer:<br>• Consulting<br>• Branding<br>• Hiring<br>• Learning<br>Each service is tailored for startups and SMEs. Read more: <a href=\"https://www.cloudcounselage.com/\">Website</a>";

        } else if (input.contains("founders") || input.contains("leadership") || input.contains("team")) {
            return "Founders:<br>• Tushar Topale (Founder & CVO)<br>• Harshada Topale (Co-Founder & Director)<br>• Subhi Shildhankar (Co-Founder & Director HR)";

        } else if (input.contains("vision") || input.contains("mission") || input.contains("2030") || input.contains("goal")) {
            return "Vision 2030:<br>Bringing 85M global jobs to India by empowering youth through internships, career development, and industry exposure.<br>Building a global network across 100+ countries.";

        } else if (input.contains("contact") || input.contains("social") || input.contains("facebook") || input.contains("instagram") || input.contains("youtube")) {
            return "Connect with us:<br>• <a href=\"https://www.facebook.com/CloudCounselage/\">Facebook</a><br>• <a href=\"https://www.instagram.com/cloudcounselage/\">Instagram</a><br>• <a href=\"https://www.youtube.com/channel/UCAZcXy22Fhj3ARp10Bem-BA\">YouTube</a><br>• <a href=\"https://www.cloudcounselage.com/\">Website</a>";

        } else if (input.contains("location") || input.contains("address") || input.contains("office")) {
            return "Our Offices:<br>• Corporate: 91 Springboard, Vikhroli West, Mumbai - 400079<br>• Registered: 91 Springboard, BKC, Mumbai - 400098";

        } else if (input.contains("ngo") || input.contains("gac") || input.contains("not for profit")) {
            return "Our NGO Partner: <b>CC-GAC</b><br>A not-for-profit guiding youth toward better careers through free professional development.<br>Visit: <a href=\"https://www.cloudcounselage.com/\">Website</a>";

        } else if (input.contains("award") || input.contains("achievement") || input.contains("news")) {
            return "Recognized by major media:<br>• CNN News18<br>• Times Now<br>• ANI<br>Awarded 'Best IT Consulting Company' by IAEA 2024.";

        } else if (input.contains("culture") || input.contains("team policies") || input.contains("genz")) {
            return "Our Culture & GenZ-Friendly Policies:<br>• No-Questions-Asked Days<br>• Break Free in a Week<br>• Build-Your-Own-Job Kit<br>• WFH, WFA, WTFlex<br>We empower you to work your way while staying aligned with our vision.";

        } else if (input.contains("thank") || input.contains("thanks")) {
            return "You're welcome! Let me know if you have more questions.";

        } else if (input.contains("hello") || input.contains("hi") || input.contains("hey")) {
            return "Hello! Ask me anything about Cloud Counselage.";

        } else {
            return "I'm sorry, I didn't understand that.<br>Try asking about:<br>• Company Info<br>• Internship<br>• Services<br>• Vision 2030<br>• Founders<br>• Contact Info<br>• Awards & Recognition<br>• Culture & Policies";
        }

    }

    public static class ChatMessage {
        private final String message;
        private final boolean isUser;

        public ChatMessage(String message, boolean isUser) {
            this.message = message;
            this.isUser = isUser;
        }

        public String getMessage() { return message; }
        public boolean isUser() { return isUser; }
    }
}
