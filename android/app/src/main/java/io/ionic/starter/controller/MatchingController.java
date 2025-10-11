package io.ionic.starter.controller;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.ionic.starter.R;
import io.ionic.starter.model.User;

public class MatchingController extends AppCompatActivity {

  private TextView name;
  private TextView bioText; // Agregado para la bio
  private ImageView imageView;
  private ImageButton nextBtn; // Cambiado de Button a ImageButton
  private ImageButton matchBtn; // Cambiado de Button a ImageButton
  private ImageButton superLikeBtn; // Agregado para Super Like
  private ImageButton backBtn;
  private ImageButton next2Btn;
  private List<User> users = new ArrayList<User>();
  private int index = 0;
  private int indexPhoto = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.matching_view);
    this.loadVars();

    String usersJson = getIntent().getStringExtra("users");
    if (usersJson == null) {
      Toast.makeText(this, "No singles available", Toast.LENGTH_SHORT).show();
      this.name.setText("No singles");
      return;
    }

    users = (ArrayList<User>) User.fromJson(usersJson);

    if (users.isEmpty()) {
      Toast.makeText(this, "No singles available", Toast.LENGTH_SHORT).show();
      this.name.setText("No singles");
      return;
    }

    this.loadPerson();
    this.setupClickListeners();
  }

  private void setupClickListeners() {
    this.nextBtn.setOnClickListener(b -> this.eventNextBtn());
    this.matchBtn.setOnClickListener(b -> this.eventMatchBtn());
    this.superLikeBtn.setOnClickListener(b -> this.eventSuperLikeBtn());
    this.backBtn.setOnClickListener(b -> this.eventBack());
    this.next2Btn.setOnClickListener(b -> this.eventNext());
  }

  private void loadPerson() {
    if (index >= users.size()) {
      return;
    }

    User currentUser = users.get(index);
    name.setText(currentUser.getName() );

    // Actualizar bio si existe
    if (bioText != null) {
      // Aquí puedes agregar lógica para mostrar intereses/pasiones
      bioText.setText(getUserInterests(currentUser));
    }

    // Cargar foto actual
    if (currentUser.getPhotos() != null && !currentUser.getPhotos().isEmpty()) {
      if (indexPhoto < currentUser.getPhotos().size()) {
        String imageUrl = currentUser.getPhotos().get(indexPhoto).getUrl();
        Picasso.get()
          .load(imageUrl)
          .placeholder(R.drawable.ic_launcher_background) // Agrega un placeholder
          .error(R.drawable.ic_launcher_background) // Agrega un error image
          .into(imageView);
      }
    }
  }

  private void eventNextBtn() {
    if (users.isEmpty() || index >= users.size() - 1) {
      Toast.makeText(this, "Sorry, this is the last user", Toast.LENGTH_SHORT).show();
      return;
    }
    index++;
    indexPhoto = 0;
    this.loadPerson();
  }

  private void eventMatchBtn() {
    if (users.isEmpty()) return;

    User matchedUser = users.get(index);
    Toast.makeText(this, "You liked " + matchedUser.getName() + "! 💖", Toast.LENGTH_SHORT).show();

    // Aquí puedes agregar lógica para guardar el match
    // matchUser(matchedUser);

    // Avanzar al siguiente usuario automáticamente
    eventNextBtn();
  }

  private void eventSuperLikeBtn() {
    if (users.isEmpty()) return;

    User superLikedUser = users.get(index);
    Toast.makeText(this, "You super liked " + superLikedUser.getName() + "! ⭐", Toast.LENGTH_SHORT).show();

    // Aquí puedes agregar lógica para super like
    // superLikeUser(superLikedUser);

    // Avanzar al siguiente usuario automáticamente
    eventNextBtn();
  }

  private void eventBack() {
    if (indexPhoto > 0) {
      indexPhoto--;
      loadPerson();
      return;
    }
    Toast.makeText(this, "This is the first photo", Toast.LENGTH_SHORT).show();
  }

  private void eventNext() {
    if (users.isEmpty()) return;

    int photoSize = getPhotoSize();
    if (indexPhoto >= photoSize - 1) {
      Toast.makeText(this, "This user doesn't have more photos", Toast.LENGTH_SHORT).show();
      return;
    }
    indexPhoto++;
    loadPerson();
  }

  private int getPhotoSize() {
    if (users.isEmpty() || index >= users.size()) {
      return 0;
    }
    User currentUser = users.get(index);
    return currentUser.getPhotos() != null ? currentUser.getPhotos().size() : 0;
  }

  private String getUserInterests(User user) {
    return "Adventure seeker 🏔️ Coffee lover ☕";
  }

  private void loadVars() {
    name = findViewById(R.id.nameText);
    imageView = findViewById(R.id.photo);
    nextBtn = findViewById(R.id.nextBtn); // ImageButton
    matchBtn = findViewById(R.id.matchBtn); // ImageButton
    superLikeBtn = findViewById(R.id.superLikeBtn); // ImageButton
    next2Btn = findViewById(R.id.next2Btn); // ImageButton
    backBtn = findViewById(R.id.backBtn); // ImageButton
  }
}
