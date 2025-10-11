package io.ionic.starter.controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.ionic.starter.R;
import io.ionic.starter.adapter.MessageAdapter;
import io.ionic.starter.service.ChatService;

public class ChatController extends AppCompatActivity {

  private static final String TAG = "ChatController";

  private TextView otherUserName;
  private ListView messagesListView;
  private EditText messageInput;
  private ImageButton sendButton;
  private ImageButton backButton;

  private ChatService chatService;
  private MessageAdapter messageAdapter;
  private String chatId;
  private String currentUserId;
  private String currentUserName;
  private String otherUserNameStr;

  private List<Map<String, Object>> messages = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_view);

    chatId = getIntent().getStringExtra("chatId");
    currentUserId = getIntent().getStringExtra("currentUserId");
    currentUserName = getIntent().getStringExtra("currentUserName");
    otherUserNameStr = getIntent().getStringExtra("otherUserName");

    Log.d(TAG, "Opening chat - ID: " + chatId + ", Current User: " + currentUserId + ", Other User: " + otherUserNameStr);

    if (chatId == null || currentUserId == null) {
      Toast.makeText(this, "Error: Missing chat data", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    chatService = new ChatService();
    initViews();
    setupClickListeners();

    messageAdapter = new MessageAdapter(this, messages, currentUserId);
    messagesListView.setAdapter(messageAdapter);

    chatService.markMessagesAsRead(chatId, currentUserId);

    listenToMessages();
  }

  private void initViews() {
    otherUserName = findViewById(R.id.otherUserName);
    messagesListView = findViewById(R.id.messagesListView);
    messageInput = findViewById(R.id.messageInput);
    sendButton = findViewById(R.id.sendButton);
    backButton = findViewById(R.id.backButton);

    if (otherUserName != null && otherUserNameStr != null) {
      otherUserName.setText(otherUserNameStr);
    }

    Log.d(TAG, "Views initialized successfully");
  }

  private void setupClickListeners() {
    backButton.setOnClickListener(v -> {
      Log.d(TAG, "Back button clicked");
      finish();
    });

    sendButton.setOnClickListener(v -> sendMessage());

    messageInput.setOnEditorActionListener((v, actionId, event) -> {
      sendMessage();
      return true;
    });
  }

  private void sendMessage() {
    String messageText = messageInput.getText().toString().trim();
    if (!TextUtils.isEmpty(messageText)) {
      Log.d(TAG, "Sending message: " + messageText);
      chatService.sendMessage(chatId, currentUserId, currentUserName, messageText);
      messageInput.setText("");
    }
  }

  private void listenToMessages() {
    Log.d(TAG, "Starting to listen to messages for chat: " + chatId);

    chatService.listenToMessages(chatId, new ChatService.FirebaseCallback<List<Map<String, Object>>>() {
      @Override
      public void onSuccess(List<Map<String, Object>> newMessages) {
        Log.d(TAG, "Messages received: " + newMessages.size() + " messages");

        runOnUiThread(() -> {
          messages.clear();
          messages.addAll(newMessages);

          if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
            // Scroll to bottom
            if (newMessages.size() > 0) {
              messagesListView.setSelection(newMessages.size() - 1);
            }
          }

          Log.d(TAG, "Adapter updated with " + messages.size() + " messages");
        });
      }

      @Override
      public void onError(Exception exception) {
        Log.e(TAG, "Error loading messages: ", exception);
        runOnUiThread(() -> {
          Toast.makeText(ChatController.this, "Error loading messages", Toast.LENGTH_SHORT).show();
        });
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "ChatController destroyed");
    if (chatService != null) {
      chatService.stopListening();
    }
  }
}
