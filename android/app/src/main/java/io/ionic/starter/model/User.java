package io.ionic.starter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String uid;
  private String name;
  private String lastName;
  private String birthDate;
  private String email;
  private String password;
  private String country;
  private String sex;
  private boolean showGenderProfile;
  private List<String> passions;
  private List<Photo> photos;

  // --- Getters y Setters ---

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public boolean isShowGenderProfile() {
    return showGenderProfile;
  }

  public void setShowGenderProfile(boolean showGenderProfile) {
    this.showGenderProfile = showGenderProfile;
  }

  public List<String> getPassions() {
    return passions;
  }

  public void setPassions(List<String> passions) {
    this.passions = passions;
  }

  public List<Photo> getPhotos() {
    return photos;
  }

  public void setPhotos(List<Photo> photos) {
    this.photos = photos;
  }
  public static List<User> fromJson(String json) {
    List<User> users = new ArrayList<>();

    try {
      JSONArray array = new JSONArray(json);

      for (int i = 0; i < array.length(); i++) {
        JSONObject obj = array.getJSONObject(i);
        User user = new User();

        user.setUid(obj.optString("uid"));
        user.setName(obj.optString("name"));
        user.setLastName(obj.optString("lastName"));
        user.setBirthDate(obj.optString("birthDate"));
        user.setEmail(obj.optString("email"));
        user.setPassword(obj.optString("password"));
        user.setCountry(obj.optString("country"));
        user.setSex(obj.optString("sex"));
        user.setShowGenderProfile(obj.optBoolean("showGenderProfile", false));

        // --- Passions ---
        List<String> passionsList = new ArrayList<>();
        JSONArray passionsArray = obj.optJSONArray("passions");
        if (passionsArray != null) {
          for (int j = 0; j < passionsArray.length(); j++) {
            passionsList.add(passionsArray.optString(j));
          }
        }
        user.setPassions(passionsList);

        // --- Photos ---
        List<Photo> photosList = new ArrayList<>();
        JSONArray photosArray = obj.optJSONArray("photos");
        if (photosArray != null) {
          for (int j = 0; j < photosArray.length(); j++) {
            JSONObject photoObj = photosArray.getJSONObject(j);
            Photo photo = new Photo();
            photo.setPath(photoObj.optString("path"));
            photo.setUrl(photoObj.optString("url"));
            photosList.add(photo);
          }
        }
        user.setPhotos(photosList);

        users.add(user);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return users;
  }
}
