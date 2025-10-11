package io.ionic.starter.service;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserService {

  private static final String TAG = "UserService";
  private final FirebaseFirestore database;

  public UserService() {
    this.database = FirebaseFirestore.getInstance();
  }

  /**
   * Obtiene todos los documentos de una colección (asíncrono)
   */
  public <T> void getAll(String nameCollection, Class<T> type, FirebaseCallback<List<T>> callback) {
    CollectionReference collectionRef = database.collection(nameCollection);

    collectionRef.get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          List<T> data = new ArrayList<>();
          for (QueryDocumentSnapshot doc : task.getResult()) {
            T item = doc.toObject(type);
            data.add(item);
          }
          Log.d(TAG, "Documentos obtenidos de " + nameCollection + ": " + data.size());
          callback.onSuccess(data);
        } else {
          Log.e(TAG, "Error al obtener documentos: ", task.getException());
          callback.onError(task.getException());
        }
      });
  }

  /**
   * Obtiene usuarios excluyendo al usuario actual (asíncrono)
   */
  public <T> void getUsersExcludingCurrent(String collectionName, String currentUserUid, Class<T> type, FirebaseCallback<List<T>> callback) {
    database.collection(collectionName)
      .get()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          List<T> users = new ArrayList<>();
          for (QueryDocumentSnapshot doc : task.getResult()) {
            if (!doc.getId().equals(currentUserUid)) {
              T user = doc.toObject(type);
              users.add(user);
            }
          }
          Log.d(TAG, "Usuarios encontrados (excluyendo " + currentUserUid + "): " + users.size());
          callback.onSuccess(users);
        } else {
          Log.e(TAG, "Error al obtener usuarios excluyendo actual: ", task.getException());
          callback.onError(task.getException());
        }
      });
  }

  /**
   * Interfaz para callbacks
   */
  public interface FirebaseCallback<T> {
    void onSuccess(T result);
    void onError(Exception exception);
  }
}
