package io.ionic.starter.controller;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.ionic.starter.R;
import io.ionic.starter.model.User;

public class MatchingController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.matching_view );
      String usersJson = getIntent().getStringExtra("users");
      var users = new ArrayList<User>();
      if (usersJson != null) {
        users = (ArrayList<User>) User.fromJson(usersJson);
      }

      TextView name = findViewById(R.id.nameText);
      name.setText(users.get(0).getName());

      ImageView imageView = findViewById(R.id.photo);

      String imageUrl = users.get(0).getPhotos().get(0).getUrl();
      Picasso.get()
        .load(imageUrl)
        .into(imageView);

    }
}
