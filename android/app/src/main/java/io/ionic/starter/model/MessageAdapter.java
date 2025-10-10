package io.ionic.starter.model;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import io.ionic.starter.R;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<Message> messages;
  private static final int VIEW_TYPE_SENT = 1;
  private static final int VIEW_TYPE_RECEIVED = 2;

  public MessageAdapter(List<Message> messages) {
    this.messages = messages;
  }

  @Override
  public int getItemViewType(int position) {
    return messages.get(position).isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_SENT) {
      View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_message_sent, parent, false);
      return new SentViewHolder(view);
    } else {
      View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_message_received, parent, false);
      return new ReceivedViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    Message message = messages.get(position);
    if (holder instanceof SentViewHolder) {
      ((SentViewHolder) holder).textView.setText(message.getText());
    } else {
      ((ReceivedViewHolder) holder).textView.setText(message.getText());
    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  static class SentViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    SentViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.textMessageSent);
    }
  }

  static class ReceivedViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ReceivedViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.textMessageReceived);
    }
  }
}
