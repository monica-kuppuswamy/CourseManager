package com.example.monic.coursemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class InstructorDetails extends Fragment {

    Instructor newInstructor;

    public InstructorDetails() {
        // Required empty public constructor
    }

    public static InstructorDetails newInstance(Instructor c) {
        InstructorDetails fragment = new InstructorDetails();
        Bundle args = new Bundle();
        args.putSerializable("instructorDetails", c);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newInstructor = (Instructor) getArguments().getSerializable("instructorDetails");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructor_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        TextView name = (TextView) getActivity().findViewById(R.id.idName);
        TextView email = (TextView) getActivity().findViewById(R.id.idEmail);
        TextView website = (TextView) getActivity().findViewById(R.id.idWebsite);
        ImageView image = (ImageView) getActivity().findViewById(R.id.imageView3);
        name.setText(newInstructor.getFirstName() + " " + newInstructor.getLastName());
        email.setText(newInstructor.getEmailId());
        website.setText(newInstructor.getWebsiteUrl());
        image.setImageURI(newInstructor.getImageUri());
        super.onActivityCreated(savedInstanceState);
    }
}
