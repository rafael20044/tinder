package io.ionic.starter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.ionic.starter.R;

public class MessageAdapter extends ArrayAdapter<Map<String, Object>> {

  private final Context context;
  private final List<Map<String, Object>> messages;
  private final LayoutInflater inflater;
  private final String currentUserId;
  private final SimpleDateFormat timeFormat;

  public MessageAdapter(Context context, List<Map<String, Object>> messages, String currentUserId) {
    super(context, R.layout.message_item, messages);
    this.context = context;
    this.messages = messages;
    this.currentUserId = currentUserId;
    this.inflater = LayoutInflater.from(context);
    this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.message_item, parent, false);
      holder = new ViewHolder();
      holder.otherMessageLayout = convertView.findViewById(R.id.otherMessageLayout);
      holder.otherMessageText = convertView.findViewById(R.id.otherMessageText);
      holder.otherMessageTime = convertView.findViewById(R.id.otherMessageTime);
      holder.myMessageLayout = convertView.findViewById(R.id.myMessageLayout);
      holder.myMessageText = convertView.findViewById(R.id.myMessageText);
      holder.myMessageTime = convertView.findViewById(R.id.myMessageTime);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    Map<String, Object> message = messages.get(position);
    bindMessageData(holder, message);

    return convertView;
  }

  private void bindMessageData(ViewHolder holder, Map<String, Object> message) {
    try {
      String senderId = (String) message.get("senderId");
      String text = (String) message.get("text");
      Object timestamp = message.get("timestamp");

      boolean isMyMessage = currentUserId.equals(senderId);

      if (isMyMessage) {
        holder.myMessageLayout.setVisibility(View.VISIBLE);
        holder.otherMessageLayout.setVisibility(View.GONE);

        holder.myMessageText.setText(text);

        if (timestamp instanceof com.google.firebase.Timestamp) {
          Date date = ((com.google.firebase.Timestamp) timestamp).toDate();
          holder.myMessageTime.setText(timeFormat.format(date));
        }
      } else {
        holder.myMessageLayout.setVisibility(View.GONE);
        holder.otherMessageLayout.setVisibility(View.VISIBLE);

        holder.otherMessageText.setText(text);

        if (timestamp instanceof com.google.firebase.Timestamp) {
          Date date = ((com.google.firebase.Timestamp) timestamp).toDate();
          holder.otherMessageTime.setText(timeFormat.format(date));
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static class ViewHolder {
    LinearLayout otherMessageLayout;
    TextView otherMessageText;
    TextView otherMessageTime;
    LinearLayout myMessageLayout;
    TextView myMessageText;
    TextView myMessageTime;
  }
}
