package com.example.monic.coursemanager;

import android.net.Uri;
import android.text.TextUtils;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monic on 11/6/2017.
 */

public class Instructor extends RealmObject implements Serializable{

    @PrimaryKey
    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailId;
    private String websiteUrl;
    private String imageUri;

    public Instructor(String userName, String firstName, String lastName, String emailId, String websiteUrl, String imageUri) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.websiteUrl = websiteUrl;
        this.imageUri = imageUri;
    }

    public Instructor() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
