package com.example.cloudcounselage;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatbotActivity.ChatMessage> chatMessages;

    public ChatAdapter(List<ChatbotActivity.ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatbotActivity.ChatMessage message = chatMessages.get(position);
        holder.messageTextView.setText(Html.fromHtml(message.getMessage()));
        holder.messageTextView.setMovementMethod(LinkMovementMethod.getInstance());


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();
        if (message.isUser()) {
            params.gravity = Gravity.END;
            holder.messageTextView.setBackgroundResource(R.color.primary_blue);
            holder.messageTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        } else {
            params.gravity = Gravity.START;
            holder.messageTextView.setBackgroundResource(R.drawable.chat_bubble);
            holder.messageTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
        }
        holder.messageTextView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        ChatViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
