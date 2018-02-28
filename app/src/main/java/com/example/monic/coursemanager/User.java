package com.example.monic.coursemanager;

import android.net.Uri;
import android.text.TextUtils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monic on 11/6/2017.
 */

public class User extends RealmObject{

    @PrimaryKey
    String userName;
    String password;
    String firstName;
    String lastName;
    String imageUri;

    public User(String firstName, String lastName, String userName, String password, String imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.imageUri = imageUri;
    }

    public User() {
    }

    public Uri getImageUri() {
        if(TextUtils.isEmpty(imageUri))
            return null;
        return  Uri.parse(imageUri);
    }

    public void setImageUri(Uri imageUri) {
        if(imageUri!=null)
            this.imageUri = imageUri.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
