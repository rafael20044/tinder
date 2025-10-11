package io.ionic.starter.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.ionic.starter.R;
import io.ionic.starter.model.User;
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
  private UserService service = new UserService();
  private int index = 0;
  private int indexPhoto = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.matching_view);
    this.loadVars();

    // Obtener datos directamente del intent
    String currentUid = getIntent().getStringExtra("uid");

    if (currentUid == null) {
      Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
      this.name.setText("No data");
      return;
    }

    // Mostrar loading
    showLoading();

    // Cargar usuarios de forma asíncrona usando getAll()
    loadUsers(currentUid);
  }

  private void loadUsers(String currentUid) {
    service.getAll("users", User.class,
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
    // Puedes mostrar un ProgressBar aquí
    name.setText("Loading...");
    // Deshabilitar botones mientras carga
    setButtonsEnabled(false);
  }

  private void hideLoading() {
    // Ocultar ProgressBar si lo agregaste
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
      // Si no hay fotos, mostrar placeholder
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

    // Aquí puedes agregar lógica para guardar el match
    // saveMatch(matchedUser);

    // Avanzar al siguiente usuario automáticamente
    eventDislikeBtn();
  }

  private void eventSuperLikeBtn() {
    if (users.isEmpty() || index >= users.size()) {
      Toast.makeText(this, "No user to super like", Toast.LENGTH_SHORT).show();
      return;
    }

    User superLikedUser = users.get(index);
    Toast.makeText(this, "You super liked " + superLikedUser.getName() + "! ⭐", Toast.LENGTH_SHORT).show();

    // Aquí puedes agregar lógica para guardar el super like
    // saveSuperLike(superLikedUser);

    // Avanzar al siguiente usuario automáticamente
    eventDislikeBtn();
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

  // Métodos opcionales para guardar matches
  private void saveMatch(User matchedUser) {
    // Implementar lógica para guardar el match en Firebase
    // service.saveMatch(currentUserId, matchedUser.getUid());
  }

  private void saveSuperLike(User superLikedUser) {
    // Implementar lógica para guardar super like en Firebase
    // service.saveSuperLike(currentUserId, superLikedUser.getUid());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (users != null) {
      users.clear();
    }
  }
}
