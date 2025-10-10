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

public class MatchingController extends AppCompatActivity {

  private TextView name;
  private ImageView imageView;
  private Button nextBtn;
  private Button matchBtn;
  private List<User> users = new ArrayList<User>();
  private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.matching_view );
      this.loadVars();
      String usersJson = getIntent().getStringExtra("users");
      if (usersJson == null) {
        Toast.makeText(this, "Don't singles", Toast.LENGTH_SHORT).show();
        this.name.setText("don't singles");
        return;
      }
      users = (ArrayList<User>) User.fromJson(usersJson);
      this.loadPerson();
      this.nextBtn.setOnClickListener(b -> this.eventNextBtn());
      matchBtn.setOnClickListener(b -> this.eventMatchtBtn());

    }

    private void loadPerson(){
      name.setText(users.get(index).getName());
      String imageUrl = users.get(index).getPhotos().get(index).getUrl();
      Picasso.get()
        .load(imageUrl)
        .into(imageView);
    }

  private void eventNextBtn(){
    var usersSize = users.size();
    if (index == usersSize - 1){
      Toast.makeText(this, "Sorry this is the last user", Toast.LENGTH_SHORT).show();
      return;
    }
    index++;
    this.loadPerson();
  }

  private void eventMatchtBtn(){
      Toast.makeText(this, "7u7", Toast.LENGTH_SHORT).show();
  }

    private void loadVars(){
      name = findViewById(R.id.nameText);
      imageView = findViewById(R.id.photo);
      nextBtn = findViewById(R.id.nextBtn);
      matchBtn = findViewById(R.id.matchBtn);
    }
}
