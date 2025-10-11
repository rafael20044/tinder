package io.ionic.starter.service;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatService {

  private static final String TAG = "ChatService";
  private final FirebaseFirestore database;
  private ListenerRegistration messagesListener;

  public ChatService() {
    this.database = FirebaseFirestore.getInstance();
  }

  public void getOrCreateChat(String user1Id, String user1Name, String user2Id, String user2Name,
                              FirebaseCallback<DocumentReference> callback) {

    // Buscar chat existente
    database.collection("chats")
      .whereArrayContains("participants", user1Id)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          for (DocumentSnapshot doc : task.getResult()) {
            List<String> participants = (List<String>) doc.get("participants");
            if (participants.contains(user2Id)) {
              // Chat existe, retornar referencia
              callback.onSuccess(doc.getReference());
              return;
            }
          }
          createNewChat(user1Id, user1Name, user2Id, user2Name, callback);
        } else {
          callback.onError(task.getException());
        }
      });
  }

  private void createNewChat(String user1Id, String user1Name, String user2Id, String user2Name,
                             FirebaseCallback<DocumentReference> callback) {
    Map<String, Object> chatData = new HashMap<>();
    chatData.put("participants", Arrays.asList(user1Id, user2Id));

    Map<String, String> participantNames = new HashMap<>();
    participantNames.put(user1Id, user1Name);
    participantNames.put(user2Id, user2Name);
    chatData.put("participantNames", participantNames);

    chatData.put("lastMessage", "");
    chatData.put("lastMessageTime", Timestamp.now());

    Map<String, Integer> unreadCount = new HashMap<>();
    unreadCount.put(user1Id, 0);
    unreadCount.put(user2Id, 0);
    chatData.put("unreadCount", unreadCount);

    chatData.put("createdAt", Timestamp.now());

    database.collection("chats")
      .add(chatData)
      .addOnSuccessListener(documentReference -> {
        callback.onSuccess(documentReference);
      })
      .addOnFailureListener(callback::onError);
  }

  /**
   * Enviar mensaje
   */
  public void sendMessage(String chatId, String senderId, String senderName, String text) {
    Map<String, Object> messageData = new HashMap<>();
    messageData.put("senderId", senderId);
    messageData.put("senderName", senderName);
    messageData.put("text", text);
    messageData.put("timestamp", Timestamp.now());
    messageData.put("read", false);
    messageData.put("type", "text");

    // Agregar mensaje a la subcolección
    database.collection("chats").document(chatId)
      .collection("messages")
      .add(messageData)
      .addOnSuccessListener(documentReference -> {
        updateLastMessage(chatId, text, senderName);
        incrementUnreadCount(chatId, senderId);
      })
      .addOnFailureListener(e -> {
        Log.e(TAG, "Error sending message: ", e);
      });
  }

  private void updateLastMessage(String chatId, String message, String senderName) {
    Map<String, Object> updates = new HashMap<>();
    updates.put("lastMessage", senderName + ": " + message);
    updates.put("lastMessageTime", Timestamp.now());

    database.collection("chats").document(chatId)
      .update(updates)
      .addOnFailureListener(e -> Log.e(TAG, "Error updating last message: ", e));
  }

  private void incrementUnreadCount(String chatId, String senderId) {
    database.collection("chats").document(chatId)
      .get()
      .addOnSuccessListener(documentSnapshot -> {
        if (documentSnapshot.exists()) {
          List<String> participants = (List<String>) documentSnapshot.get("participants");
          for (String participantId : participants) {
            if (!participantId.equals(senderId)) {
              Map<String, Object> updates = new HashMap<>();
              updates.put("unreadCount." + participantId, FieldValue.increment(1));
              database.collection("chats").document(chatId)
                .update(updates);
            }
          }
        }
      });
  }

  public void listenToMessages(String chatId, FirebaseCallback<List<Map<String, Object>>> callback) {
    // Remover listener anterior si existe
    if (messagesListener != null) {
      messagesListener.remove();
    }

    messagesListener = database.collection("chats").document(chatId)
      .collection("messages")
      .orderBy("timestamp", Query.Direction.ASCENDING)
      .addSnapshotListener((snapshot, error) -> {
        if (error != null) {
          callback.onError(error);
          return;
        }

        if (snapshot != null) {
          List<Map<String, Object>> messages = new ArrayList<>();
          for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Map<String, Object> message = doc.getData();
            message.put("id", doc.getId());
            messages.add(message);
          }
          callback.onSuccess(messages);
        }
      });
  }

  public void getUserChats(String userId, FirebaseCallback<List<Map<String, Object>>> callback) {
    database.collection("chats")
      .whereArrayContains("participants", userId)
      .orderBy("lastMessageTime", Query.Direction.DESCENDING)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          List<Map<String, Object>> chats = new ArrayList<>();
          for (DocumentSnapshot doc : task.getResult()) {
            Map<String, Object> chat = doc.getData();
            chat.put("id", doc.getId());

            // Obtener nombre del otro participante
            Map<String, String> participantNames = (Map<String, String>) chat.get("participantNames");
            String otherUserName = "";
            for (Map.Entry<String, String> entry : participantNames.entrySet()) {
              if (!entry.getKey().equals(userId)) {
                otherUserName = entry.getValue();
                break;
              }
            }
            chat.put("otherUserName", otherUserName);

            chats.add(chat);
          }
          callback.onSuccess(chats);
        } else {
          callback.onError(task.getException());
        }
      });
  }


  public void markMessagesAsRead(String chatId, String userId) {
    // Resetear contador de no leídos
    Map<String, Object> updates = new HashMap<>();
    updates.put("unreadCount." + userId, 0);

    database.collection("chats").document(chatId)
      .update(updates)
      .addOnFailureListener(e -> Log.e(TAG, "Error marking messages as read: ", e));
  }

  public void stopListening() {
    if (messagesListener != null) {
      messagesListener.remove();
      messagesListener = null;
    }
  }

  public interface FirebaseCallback<T> {
    void onSuccess(T result);
    void onError(Exception exception);
  }
}
