package io.ionic.starter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.ionic.starter.R;
import io.ionic.starter.controller.ChatListController;

public class ChatAdapter extends ArrayAdapter<Map<String, Object>> {

  private final Context context;
  private final List<Map<String, Object>> chats;
  private final LayoutInflater inflater;
  private final String currentUserId;

  public ChatAdapter(Context context, List<Map<String, Object>> chats, String currentUserId) {
    super(context, R.layout.chat_list_item, chats);
    this.context = context;
    this.chats = chats;
    this.currentUserId = currentUserId;
    this.inflater = LayoutInflater.from(context);
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.chat_list_item, parent, false);
      holder = new ViewHolder();
      holder.userName = convertView.findViewById(R.id.userName);
      holder.lastMessage = convertView.findViewById(R.id.lastMessage);
      holder.lastMessageTime = convertView.findViewById(R.id.lastMessageTime);
      holder.unreadBadge = convertView.findViewById(R.id.unreadBadge);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    Map<String, Object> chat = chats.get(position);
    bindChatData(holder, chat);

    convertView.setOnClickListener(v -> {

      // Llamar al método openChat directamente
      if (context instanceof ChatListController) {
        ((ChatListController) context).openChatDirect(chat);
      }
    });

    return convertView;
  }

  private void bindChatData(ViewHolder holder, Map<String, Object> chat) {
    try {
      String otherUserName = (String) chat.get("otherUserName");
      holder.userName.setText(otherUserName != null ? otherUserName : "Unknown User");

      String lastMessage = (String) chat.get("lastMessage");
      holder.lastMessage.setText(lastMessage != null && !lastMessage.isEmpty() ?
        lastMessage : "Start a conversation...");

      Object lastMessageTime = chat.get("lastMessageTime");
      if (lastMessageTime instanceof com.google.firebase.Timestamp) {
        Date date = ((com.google.firebase.Timestamp) lastMessageTime).toDate();
        holder.lastMessageTime.setText(formatTime(date));
      } else {
        holder.lastMessageTime.setText("");
      }

      if (currentUserId != null) {
        Map<String, Object> unreadCount = (Map<String, Object>) chat.get("unreadCount");
        if (unreadCount != null) {
          Object unreadObj = unreadCount.get(currentUserId);
          if (unreadObj instanceof Long) {
            long unread = (Long) unreadObj;
            if (unread > 0) {
              holder.unreadBadge.setText(String.valueOf(unread));
              holder.unreadBadge.setVisibility(View.VISIBLE);
              return;
            }
          } else if (unreadObj instanceof Integer) {
            int unread = (Integer) unreadObj;
            if (unread > 0) {
              holder.unreadBadge.setText(String.valueOf(unread));
              holder.unreadBadge.setVisibility(View.VISIBLE);
              return;
            }
          }
        }
      }
      holder.unreadBadge.setVisibility(View.GONE);

    } catch (Exception e) {
      e.printStackTrace();
      // Valores por defecto en caso de error
      holder.userName.setText("Error loading chat");
      holder.lastMessage.setText("");
      holder.lastMessageTime.setText("");
      holder.unreadBadge.setVisibility(View.GONE);
    }
  }

  private String formatTime(Date date) {
    try {
      long diff = System.currentTimeMillis() - date.getTime();
      long minutes = diff / (60 * 1000);
      long hours = minutes / 60;
      long days = hours / 24;

      if (days > 0) {
        return days + "d ago";
      } else if (hours > 0) {
        return hours + "h ago";
      } else if (minutes > 0) {
        return minutes + "m ago";
      } else {
        return "Just now";
      }
    } catch (Exception e) {
      return "";
    }
  }

  private static class ViewHolder {
    TextView userName;
    TextView lastMessage;
    TextView lastMessageTime;
    TextView unreadBadge;
  }
}
