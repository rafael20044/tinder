package io.ionic.starter.controller;

import android.os.Bundle;
import android.widget.Button;
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
  private ImageView imageView;
  private Button nextBtn;
  private Button matchBtn;
  private ImageButton backBtn;
  private  ImageButton next2Btn;
  private List<User> users = new ArrayList<User>();
  private int index = 0;
  private int indexPhoto = 0;

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
      backBtn.setOnClickListener(b -> this.eventBack());
      next2Btn.setOnClickListener(b -> this.eventNext());
    }

    private void loadPerson(){
      name.setText(users.get(index).getName());
      String imageUrl = users.get(index).getPhotos().get(indexPhoto).getUrl();
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
    indexPhoto = 0;
    this.loadPerson();
  }

  private void eventMatchtBtn(){
      Toast.makeText(this, "7u7", Toast.LENGTH_SHORT).show();
  }

  private void eventBack(){
    if (indexPhoto > 0){
      indexPhoto--;
      loadPerson();
      return;
    }
  }

  private void eventNext(){
    var photoSize = getPhotoSize();
    if (indexPhoto == photoSize - 1){
      Toast.makeText(this, "This user don't have more photos", Toast.LENGTH_SHORT).show();
      return;
    }
    indexPhoto++;
    loadPerson();
  }


  private int getPhotoSize(){
      return users.get(index).getPhotos().size();
  }

    private void loadVars(){
      name = findViewById(R.id.nameText);
      imageView = findViewById(R.id.photo);
      nextBtn = findViewById(R.id.nextBtn);
      matchBtn = findViewById(R.id.matchBtn);
      next2Btn = findViewById(R.id.next2Btn);
      backBtn = findViewById(R.id.backBtn);
    }
}
