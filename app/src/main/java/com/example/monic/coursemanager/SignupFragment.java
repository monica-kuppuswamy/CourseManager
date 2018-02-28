package com.example.monic.coursemanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
// */
public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Realm realm;

    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo;
    Uri imageURI;
    ImageView camera;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final EditText firstName = (EditText) getActivity().findViewById(R.id.firstName);
        final EditText lastName = (EditText) getActivity().findViewById(R.id.lastName);
        final EditText userName = (EditText) getActivity().findViewById(R.id.uName);
        final EditText password = (EditText) getActivity().findViewById(R.id.lpassword);
        camera = (ImageView) getActivity().findViewById(R.id.imageView);
        Button register = (Button) getActivity().findViewById(R.id.buttonRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String uName = userName.getText().toString();
                String uPassword = password.getText().toString();
                if(fName.length() == 0 || lName.length() == 0 || uName.length() == 0 || uPassword.length() == 0) {
                    Toast.makeText(getActivity(), "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                } else {
                    if(uPassword.length() < 8) {
                        Toast.makeText(getActivity(), "Password must contain atleast 8 characters.", Toast.LENGTH_SHORT).show();
                    } else {
                        realm = Realm.getDefaultInstance();
                        RealmResults<User> olduser = realm.where(User.class).equalTo("userName", userName.getText().toString())
                                .findAll();
                        if(olduser.size() == 0) {
                            User newUser = new User();
                            newUser.setFirstName(firstName.getText().toString());
                            newUser.setLastName(lastName.getText().toString());
                            newUser.setUserName(userName.getText().toString());
                            newUser.setPassword(password.getText().toString());
                            newUser.setImageUri(imageURI);
                            mListener.registerUser(newUser);
                            mListener.gotoCourseManagerFromSignup(newUser.getUserName());
                        } else {
                            Toast.makeText(getActivity(), "Username already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            1001);
                }
                else {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("demo","called");

        // Adding Image shot on Camera
        if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d("demo","passed");
            photo = (Bitmap) intent.getExtras().get("data");
            camera.setImageBitmap(photo);
            imageURI = getImageUri(getActivity().getApplicationContext(), photo);
            camera.setImageURI(imageURI);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                } else {

                    Toast.makeText(getActivity(), "Please grant camera permissions", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 1002: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    imageURI = getImageUri(getActivity().getApplicationContext(), photo);
                    camera.setImageURI(imageURI);


                } else {

                    Toast.makeText(getActivity(), "Please grant camera permissions", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1002);
        }
        else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void registerUser(User user);
        void gotoCourseManagerFromSignup(String userName);
    }
}
