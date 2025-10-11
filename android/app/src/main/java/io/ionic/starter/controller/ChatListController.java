package io.ionic.starter.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ionic.starter.R;
import io.ionic.starter.adapter.ChatAdapter;
import io.ionic.starter.service.ChatService;

public class ChatListController extends AppCompatActivity {

  private static final String TAG = "ChatListController";

  private ListView chatsListView;
  private LinearLayout emptyState;
  private ProgressBar loadingProgress;
  private Button startMatchingBtn;
  private TextView emptyStateTitle, emptyStateMessage;
  private ChatService chatService;
  private String currentUserId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.chat_list_view);

    initViews();

    chatService = new ChatService();
    currentUserId = getIntent().getStringExtra("uid");

    if (currentUserId == null) {
      Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show();
      return;
    }
    Log.d(TAG, "Loading chats for user: " + currentUserId);

    loadUserChats();
  }

  private void initViews() {
    Log.d(TAG, "=== INIT VIEWS START ===");

    try {
      chatsListView = findViewById(R.id.chatsListView);
      Log.d(TAG, "chatsListView found: " + (chatsListView != null));


      if (chatsListView != null) {
        Log.d(TAG, "ListView clickable: " + chatsListView.isClickable());
        Log.d(TAG, "ListView enabled: " + chatsListView.isEnabled());
        Log.d(TAG, "ListView itemsClickable: " + chatsListView.getItemsCanFocus());


        chatsListView.setItemsCanFocus(true);
        chatsListView.setClickable(true);
        chatsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
              Object item = parent.getItemAtPosition(position);

              if (item instanceof Map) {
                Map<String, Object> chat = (Map<String, Object>) item;
                openChat(chat);
              } else {

                // Debug: ver qué hay en el adapter
                BaseAdapter adapter = (BaseAdapter) parent.getAdapter();

                for (int i = 0; i < adapter.getCount(); i++) {
                  Object adapterItem = adapter.getItem(i);
                  Log.d(TAG, "Adapter item " + i + ": " + adapterItem + " (" +
                    (adapterItem != null ? adapterItem.getClass().getSimpleName() : "null") + ")");
                }
              }
            } catch (Exception e) {
              Log.e(TAG, "Error in onItemClick: ", e);
            }
          }
        });
        Log.d(TAG, "ListView click listener SET");
      }

    } catch (Exception e) {
      Log.e(TAG, "Error in initViews: ", e);
    }

    Log.d(TAG, "=== INIT VIEWS COMPLETE ===");
  }

  private void openChat(Map<String, Object> chat) {
    try {
      String chatId = (String) chat.get("id");
      String otherUserName = (String) chat.get("otherUserName");


      if (chatId == null) {
        Toast.makeText(this, "Chat ID not found", Toast.LENGTH_SHORT).show();
        return;
      }

      try {
        Class.forName("io.ionic.starter.controller.ChatController");
      } catch (ClassNotFoundException e) {
        Log.e(TAG, "ERROR: ChatController class not found!", e);
        Toast.makeText(this, "Chat feature not available", Toast.LENGTH_SHORT).show();
        return;
      }

      Intent intent = new Intent(this, ChatController.class);
      intent.putExtra("chatId", chatId);
      intent.putExtra("currentUserId", currentUserId);
      intent.putExtra("currentUserName", getIntent().getStringExtra("userName"));
      intent.putExtra("otherUserName", otherUserName != null ? otherUserName : "Unknown User");

      startActivity(intent);


    } catch (Exception e) {
      Log.e(TAG, "ERROR in openChat: ", e);
      Toast.makeText(this, "Error opening chat: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  private void loadUserChats() {
    showLoading();

    chatService.getUserChats(currentUserId, new ChatService.FirebaseCallback<List<Map<String, Object>>>() {
      @Override
      public void onSuccess(List<Map<String, Object>> chats) {
        Log.d(TAG, "Chats loaded successfully: " + chats.size() + " chats found");
        runOnUiThread(() -> {
          hideLoading();
          updateChatsList(chats);
        });
      }

      @Override
      public void onError(Exception exception) {
        Log.e(TAG, "Error loading chats: ", exception);
        runOnUiThread(() -> {
          hideLoading();
          Toast.makeText(ChatListController.this,
            "Error loading chats: " + exception.getMessage(),
            Toast.LENGTH_LONG).show();
          showEmptyState();
        });
      }
    });
  }

  private void updateChatsList(List<Map<String, Object>> chats) {
    if (chats == null || chats.isEmpty()) {
      Log.d(TAG, "No chats found, showing empty state");
      showEmptyState();
    } else {
      Log.d(TAG, "Showing " + chats.size() + " chats");
      showChatsList();
      try {
        ChatAdapter adapter = new ChatAdapter(this, chats, currentUserId);
        chatsListView.setAdapter(adapter);
      } catch (Exception e) {
        Log.e(TAG, "Error setting adapter: ", e);
        Toast.makeText(this, "Error displaying chats", Toast.LENGTH_SHORT).show();
        showEmptyState();
      }
    }
  }

  private void showLoading() {
    if (loadingProgress != null) {
      loadingProgress.setVisibility(View.VISIBLE);
    }
    if (chatsListView != null) {
      chatsListView.setVisibility(View.GONE);
    }
    if (emptyState != null) {
      emptyState.setVisibility(View.GONE);
    }
  }

  private void hideLoading() {
    if (loadingProgress != null) {
      loadingProgress.setVisibility(View.GONE);
    }
  }

  private void showChatsList() {
    if (chatsListView != null) {
      chatsListView.setVisibility(View.VISIBLE);
    }
    if (emptyState != null) {
      emptyState.setVisibility(View.GONE);
    }
  }

  private void showEmptyState() {
    if (chatsListView != null) {
      chatsListView.setVisibility(View.GONE);
    }
    if (emptyState != null) {
      emptyState.setVisibility(View.VISIBLE);
    }

    if (emptyStateTitle != null) {
      emptyStateTitle.setText("No messages yet");
    }
    if (emptyStateMessage != null) {
      emptyStateMessage.setText("Start matching with people to see your messages here");
    }
  }

  public void openChatDirect(Map<String, Object> chat) {
    openChat(chat);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (chatService != null) {
      chatService.stopListening();
    }
  }
}
