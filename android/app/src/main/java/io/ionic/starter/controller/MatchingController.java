package io.ionic.starter.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ionic.starter.R;
import io.ionic.starter.model.User;
import io.ionic.starter.service.ChatService;
import io.ionic.starter.service.UserService;

public class MatchingController extends AppCompatActivity {

  private TextView name;
  private ImageView imageView;
  private Button dislikeBtn;
  private Button likeBtn;
  private Button superLikeBtn;
  private Button backBtn;
  private Button next2Btn;
  private List<User> users = new ArrayList<User>();
  private UserService userService = new UserService();
  private ChatService chatService = new ChatService(); // Agregar ChatService
  private int index = 0;
  private int indexPhoto = 0;
  private String currentUserId;
  private String currentUserName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.matching_view);
    this.loadVars();

    // Obtener datos directamente del intent
    currentUserId = getIntent().getStringExtra("uid");
    currentUserName = getIntent().getStringExtra("name");

    if (currentUserId == null) {
      Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
      this.name.setText("No data");
      return;
    }

    showLoading();

    loadUsers(currentUserId);
  }

  private void loadUsers(String currentUid) {
    userService.getAll("users", User.class,
      new UserService.FirebaseCallback<List<User>>() {
        @Override
        public void onSuccess(List<User> result) {
          runOnUiThread(() -> {
            hideLoading();

            // Usar filterCurrentUser como antes
            users = filterCurrentUser(result, currentUid);

            if (users.isEmpty()) {
              Toast.makeText(MatchingController.this, "No singles available", Toast.LENGTH_SHORT).show();
              name.setText("No singles");
              return;
            }

            setupClickListeners();
            loadPerson();
          });
        }

        @Override
        public void onError(Exception exception) {
          runOnUiThread(() -> {
            hideLoading();
            Toast.makeText(MatchingController.this, "Error loading users: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            name.setText("Error");
          });
        }
      });
  }

  private List<User> filterCurrentUser(List<User> allUsers, String currentUid) {
    List<User> filteredUsers = new ArrayList<>();
    for (User user : allUsers) {
      if (user.getUid() != null && !user.getUid().equals(currentUid)) {
        filteredUsers.add(user);
      }
    }
    return filteredUsers;
  }

  private void showLoading() {
    name.setText("Loading...");
    setButtonsEnabled(false);
  }

  private void hideLoading() {
    setButtonsEnabled(true);
  }

  private void setButtonsEnabled(boolean enabled) {
    if (dislikeBtn != null) dislikeBtn.setEnabled(enabled);
    if (likeBtn != null) likeBtn.setEnabled(enabled);
    if (superLikeBtn != null) superLikeBtn.setEnabled(enabled);
    if (backBtn != null) backBtn.setEnabled(enabled);
    if (next2Btn != null) next2Btn.setEnabled(enabled);
  }

  private void setupClickListeners() {
    this.dislikeBtn.setOnClickListener(b -> this.eventDislikeBtn());
    this.likeBtn.setOnClickListener(b -> this.eventLikeBtn());
    this.superLikeBtn.setOnClickListener(b -> this.eventSuperLikeBtn());
    this.backBtn.setOnClickListener(b -> this.eventBack());
    this.next2Btn.setOnClickListener(b -> this.eventNext());
  }

  private void loadPerson() {
    if (index >= users.size()) {
      Toast.makeText(this, "No more users", Toast.LENGTH_SHORT).show();
      return;
    }

    User currentUser = users.get(index);
    name.setText(currentUser.getName());

    if (currentUser.getPhotos() != null && !currentUser.getPhotos().isEmpty()) {
      if (indexPhoto < currentUser.getPhotos().size()) {
        String imageUrl = currentUser.getPhotos().get(indexPhoto).getUrl();
        Picasso.get()
          .load(imageUrl)
          .placeholder(R.drawable.ic_launcher_background)
          .error(R.drawable.ic_launcher_background)
          .into(imageView);
      } else {
        indexPhoto = 0;
        String imageUrl = currentUser.getPhotos().get(0).getUrl();
        Picasso.get()
          .load(imageUrl)
          .placeholder(R.drawable.ic_launcher_background)
          .error(R.drawable.ic_launcher_background)
          .into(imageView);
      }
    } else {
      imageView.setImageResource(R.drawable.ic_launcher_background);
    }
  }

  private void eventDislikeBtn() {
    if (users.isEmpty()) {
      Toast.makeText(this, "No users available", Toast.LENGTH_SHORT).show();
      return;
    }

    if (index >= users.size() - 1) {
      Toast.makeText(this, "Sorry, this is the last user", Toast.LENGTH_SHORT).show();
      return;
    }

    index++;
    indexPhoto = 0;
    this.loadPerson();
  }

  private void eventLikeBtn() {
    if (users.isEmpty() || index >= users.size()) {
      Toast.makeText(this, "No user to like", Toast.LENGTH_SHORT).show();
      return;
    }

    User matchedUser = users.get(index);
    Toast.makeText(this, "You liked " + matchedUser.getName() + "! 💖", Toast.LENGTH_SHORT).show();

    createChatWithUser(matchedUser);

    eventDislikeBtn();
  }

  private void eventSuperLikeBtn() {
    if (users.isEmpty() || index >= users.size()) {
      Toast.makeText(this, "No user to super like", Toast.LENGTH_SHORT).show();
      return;
    }

    User superLikedUser = users.get(index);
    Toast.makeText(this, "You super liked " + superLikedUser.getName() + "! ⭐", Toast.LENGTH_SHORT).show();

    // También crear chat con super like
    createChatWithUser(superLikedUser);

    // Avanzar al siguiente usuario automáticamente
    eventDislikeBtn();
  }

  private void createChatWithUser(User matchedUser) {
    if (currentUserId == null || currentUserName == null) {
      Toast.makeText(this, "Cannot create chat: user data missing", Toast.LENGTH_SHORT).show();
      return;
    }

    chatService.getOrCreateChat(currentUserId, currentUserName,
      matchedUser.getUid(), matchedUser.getName(),
      new ChatService.FirebaseCallback<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference chatRef) {
          runOnUiThread(() -> {
            // Opcional: mostrar mensaje de que el chat fue creado
            Toast.makeText(MatchingController.this,
              "Chat created with " + matchedUser.getName() + "!",
              Toast.LENGTH_SHORT).show();

            // Aquí podrías guardar también el match en una colección separada
            saveMatch(matchedUser, chatRef.getId());
          });
        }

        @Override
        public void onError(Exception exception) {
          runOnUiThread(() -> {
            Toast.makeText(MatchingController.this,
              "Error creating chat: " + exception.getMessage(),
              Toast.LENGTH_SHORT).show();
          });
        }
      });
  }

  private void saveMatch(User matchedUser, String chatId) {
    // Puedes implementar esto para tener un historial de matches
    // Por ejemplo, en una colección "matches"
    Map<String, Object> matchData = new HashMap<>();
    matchData.put("user1Id", currentUserId);
    matchData.put("user2Id", matchedUser.getUid());
    matchData.put("user1Name", currentUserName);
    matchData.put("user2Name", matchedUser.getName());
    matchData.put("chatId", chatId);
    matchData.put("matchedAt", com.google.firebase.Timestamp.now());
    matchData.put("active", true);
  }

  private void eventBack() {
    if (indexPhoto > 0) {
      indexPhoto--;
      loadPerson();
    } else {
      Toast.makeText(this, "This is the first photo", Toast.LENGTH_SHORT).show();
    }
  }

  private void eventNext() {
    if (users.isEmpty() || index >= users.size()) {
      Toast.makeText(this, "No user available", Toast.LENGTH_SHORT).show();
      return;
    }

    User currentUser = users.get(index);
    if (currentUser.getPhotos() == null || currentUser.getPhotos().isEmpty()) {
      Toast.makeText(this, "This user has no photos", Toast.LENGTH_SHORT).show();
      return;
    }

    int photoSize = currentUser.getPhotos().size();
    if (indexPhoto >= photoSize - 1) {
      Toast.makeText(this, "This user doesn't have more photos", Toast.LENGTH_SHORT).show();
      return;
    }

    indexPhoto++;
    loadPerson();
  }

  private void loadVars() {
    name = findViewById(R.id.nameText);
    imageView = findViewById(R.id.photo);
    dislikeBtn = findViewById(R.id.dislikeBtn);
    likeBtn = findViewById(R.id.likeBtn);
    superLikeBtn = findViewById(R.id.superLikeBtn);
    next2Btn = findViewById(R.id.next2Btn);
    backBtn = findViewById(R.id.backBtn);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (users != null) {
      users.clear();
    }
    chatService.stopListening();
  }
}
