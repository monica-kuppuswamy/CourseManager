package com.example.monic.coursemanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        SignupFragment.OnFragmentInteractionListener, CourseManager.OnFragmentInteractionListener,
        AddInstructor.OnFragmentInteractionListener, CreateCourse.OnFragmentInteractionListener,
        InstructorCourseAdapter.AdapterListener, CourseAdapter.CourseInterface, AddAdapter.InstructorInterface{

    Realm realm;
    int selectedInstructorId = -1;
    String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
        Realm.init(MainActivity.this);
        realm = Realm.getDefaultInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment(), "login")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.addInstructors:
                String title = getSupportActionBar().getTitle().toString();
                if(!(title.equalsIgnoreCase("login")) && !(title.equalsIgnoreCase("sign up"))) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, AddInstructor.newInstance(currentUserName), "addinstructors")
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(MainActivity.this, "Please login or signup to continue.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.instructors:
                String actionTitle = getSupportActionBar().getTitle().toString();
                if(!(actionTitle.equalsIgnoreCase("login")) && !(actionTitle.equalsIgnoreCase("sign up"))) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ViewInstructor.newInstance(currentUserName), "viewinstructors")
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(MainActivity.this, "Please login or signup to continue.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.home:
                String aTitle = getSupportActionBar().getTitle().toString();
                if(!(aTitle.equalsIgnoreCase("login")) && !(aTitle.equalsIgnoreCase("sign up"))) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, CourseManager.newInstance(currentUserName), "coursemanager")
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(MainActivity.this, "Please login or signup to continue.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.logout:
                String toolTitle = getSupportActionBar().getTitle().toString();
                if(!(toolTitle.equalsIgnoreCase("login")) && !(toolTitle.equalsIgnoreCase("sign up"))) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new LoginFragment(), "login")
                            .commit();
                } else {
                    Toast.makeText(MainActivity.this, "Please login to logout.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.quit:
                finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void gotoCourseManagerFromSignup(String userName) {
        getSupportActionBar().setTitle("Course Manager");
        currentUserName = userName;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CourseManager.newInstance(userName), "coursemanager")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerUser(User user) {
        realm.beginTransaction();
        final User newUser = realm.copyToRealmOrUpdate(user); // Persist unmanaged objects
        realm.commitTransaction();
    }

    @Override
    public void goToSignUpPage() {
        getSupportActionBar().setTitle("Sign Up");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignupFragment(), "signup")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean isValidUser(String username, String password) {
        RealmResults<User> user = realm.where(User.class).equalTo("userName", username).findAll();
        if(user.size() == 1 && user.get(0).getPassword().equals(password))
            return true;
        else
            return false;
    }

    @Override
    public void goToCourseManagerFromLogin(String user) {
        getSupportActionBar().setTitle("Course Manager");
        currentUserName = user;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CourseManager.newInstance(user), "coursemanager")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoCreateCourse(String currentUser) {
        getSupportActionBar().setTitle("Create Course");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CreateCourse.newInstance(currentUser), "createcourse")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void saveInstructor(Instructor i) {
        realm.beginTransaction();
        final Instructor newInstructor = realm.copyToRealmOrUpdate(i); // Persist unmanaged objects
        realm.commitTransaction();
    }

    @Override
    public void saveCourse(Course c) {
        realm.beginTransaction();
        final Course newCourse = realm.copyToRealmOrUpdate(c); // Persist unmanaged objects
        realm.commitTransaction();
    }

    @Override
    public int getSelectedInstructorId() {
        return selectedInstructorId;
    }

    @Override
    public void setInstructorId(int id) {
        selectedInstructorId = id;
    }

    @Override
    public void displayCourseDetails(Course c) {
        getSupportActionBar().setTitle("Course Details");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CourseDetails.newInstance(c), "coursedetails")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void refreshCoursePage() {
        getSupportActionBar().setTitle("Course Manager");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, CourseManager.newInstance(currentUserName), "coursemanager")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void displayInstructorDetails(Instructor c) {
        getSupportActionBar().setTitle("Instructor Details");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, InstructorDetails.newInstance(c), "instructordetails")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void refreshPage() {
        getSupportActionBar().setTitle("Instructors");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ViewInstructor(), "viewinstructors")
                .addToBackStack(null)
                .commit();
    }
}
