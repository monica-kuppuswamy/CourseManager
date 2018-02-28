package com.example.monic.coursemanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.Realm;

public class CourseDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Course newCourse;
    Realm realm;

    public CourseDetails() {
        // Required empty public constructor
    }

    public static CourseDetails newInstance(Course c) {
        CourseDetails fragment = new CourseDetails();
        Bundle args = new Bundle();
        args.putSerializable("courseDetails", c);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseDetails newInstance(String param1, String param2) {
        CourseDetails fragment = new CourseDetails();
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
            newCourse = (Course) getArguments().getSerializable("courseDetails");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        TextView courseTitle = (TextView) getActivity().findViewById(R.id.cdTitle);
        TextView instructorName = (TextView) getActivity().findViewById(R.id.cInstructor);
        TextView courseDay = (TextView) getActivity().findViewById(R.id.cdDay);
        TextView courseTime = (TextView) getActivity().findViewById(R.id.cdTime);
        TextView semester = (TextView) getActivity().findViewById(R.id.cdSemster);
        TextView courseCredit = (TextView) getActivity().findViewById(R.id.cdCredit);

        courseTitle.setText(newCourse.getTitle());
        realm = Realm.getDefaultInstance();
        Instructor i = realm.where(Instructor.class).equalTo("id", newCourse.getInstructorId()).findFirst();
        instructorName.setText(i.getFirstName() + " " + i.getLastName());
        courseDay.setText(newCourse.getDay());
        courseTime.setText(newCourse.getTime());
        semester.setText(newCourse.getSemester());
        courseCredit.setText(Integer.toString(newCourse.getCredits()));
        super.onActivityCreated(savedInstanceState);
    }
}
