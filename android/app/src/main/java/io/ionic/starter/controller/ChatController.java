package io.ionic.starter.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.ionic.starter.R;
import io.ionic.starter.model.Message;
import io.ionic.starter.model.MessageAdapter;

public class ChatController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.chat_view);
      RecyclerView recyclerView = findViewById(R.id.recyclerViewChat);
      EditText editMessage = findViewById(R.id.editMessage);
      Button btnSend = findViewById(R.id.btnSend);

      var messages = new ArrayList<Message>();
      var adapter = new MessageAdapter(messages);

      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(adapter);

      btnSend.setOnClickListener(v -> {
        String text = editMessage.getText().toString().trim();
        if (!text.isEmpty()) {
          messages.add(new Message(text, true));
          messages.add(new Message( text, false));
          adapter.notifyDataSetChanged();
          recyclerView.scrollToPosition(messages.size() - 1);
          editMessage.setText("");
        }
      });
    }
}
