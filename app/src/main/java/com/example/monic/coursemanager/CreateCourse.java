package com.example.monic.coursemanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link CreateCourse.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link CreateCourse#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CreateCourse extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Realm realm;
    ArrayAdapter<CharSequence> adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    String currentUser;
    TextView noInstructors;
    Button create;

    public CreateCourse() {
        // Required empty public constructor
    }

    public static CreateCourse newInstance(String uName) {
        CreateCourse fragment = new CreateCourse();
        Bundle args = new Bundle();
        args.putString("user", uName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateCourse.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCourse newInstance(String param1, String param2) {
        CreateCourse fragment = new CreateCourse();
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
            currentUser = getArguments().getString("user");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final EditText cTitle = (EditText) getActivity().findViewById(R.id.cTitle);
        final EditText hourText = (EditText) getActivity().findViewById(R.id.hourText);
        final EditText minText = (EditText) getActivity().findViewById(R.id.minText);
        final RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.creditGroup);

        final Spinner daySpinner = (Spinner) getActivity().findViewById(R.id.daysSpinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        final Spinner timeSpinner = (Spinner) getActivity().findViewById(R.id.timeSpinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        final Spinner semSpinner = (Spinner) getActivity().findViewById(R.id.semSpinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.semester_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapter);

        noInstructors = (TextView) getActivity().findViewById(R.id.noInstructors);
        noInstructors.setVisibility(View.INVISIBLE);
        create = (Button) getActivity().findViewById(R.id.createButton);
        Button reset = (Button) getActivity().findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cTitle.setText("");

                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.semester_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                semSpinner.setAdapter(adapter);

                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.time_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                timeSpinner.setAdapter(adapter);

                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.days_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                daySpinner.setAdapter(adapter);
                hourText.setText("");
                minText.setText("");
                radioGroup.clearCheck();
                displayInstructorsForCourse();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String courseTitle = cTitle.getText().toString();
                    int teacherId = mListener.getSelectedInstructorId();
                    String courseDay = daySpinner.getItemAtPosition(daySpinner.getSelectedItemPosition())
                            .toString();
                    int hour;
                    int min;
                    String courseSemester = semSpinner.getItemAtPosition(semSpinner.getSelectedItemPosition())
                            .toString();
                    int courseCredit;

                    realm = Realm.getDefaultInstance();
                    Number currentIdNum = realm.where(Course.class).max("id");
                    int nextId;
                    if(currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    if(courseTitle.length() != 0) {
                        if(teacherId != -1) {
                            if(!(courseDay.equals("Select"))) {
                                try {
                                    hour = Integer.parseInt(hourText.getText().toString());
                                    min = Integer.parseInt(minText.getText().toString());
                                    if(!(hour >= 1 && hour <= 12) || !(min >= 0 && min <= 59)) {
                                        Toast.makeText(getActivity(), "Invalid input for time.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(courseSemester.equals("Select Semester")) {
                                            Toast.makeText(getActivity(), "Select semester", Toast.LENGTH_SHORT).show();
                                        } else {
                                            int selectedId = radioGroup.getCheckedRadioButtonId();
                                            RadioButton radioButton = (RadioButton) getActivity().findViewById(selectedId);
                                            try {
                                                courseCredit = Integer.parseInt(radioButton.getText().toString());

                                                List<Course> coursesList = new ArrayList<>();
                                                coursesList = realm.where(Course.class)
                                                        .equalTo("userName", currentUser).findAll();

                                                boolean isPresent = false;
                                                for(Course c : coursesList) {
                                                    if(c.getTitle().equalsIgnoreCase(courseTitle)) {
                                                        isPresent = true;
                                                        break;
                                                    }
                                                }

                                                if(isPresent) {
                                                    Toast.makeText(getActivity(), "Course already exists.", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Course course = new Course();
                                                    course.setId(nextId);
                                                    course.setUserName(currentUser);
                                                    course.setTitle(courseTitle);
                                                    course.setInstructorId(teacherId + 1);
                                                    course.setDay(courseDay);
                                                    course.setTime(hourText.getText().toString() + ":" + minText.getText().toString() +
                                                        timeSpinner.getItemAtPosition(timeSpinner.getSelectedItemPosition()).toString());
                                                    course.setSemester(courseSemester);
                                                    course.setCredits(courseCredit);
                                                    mListener.saveCourse(course);
                                                    Toast.makeText(getActivity(), "Course added to your list.", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "Choose credit for the course.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } catch(Exception e) {
                                    Toast.makeText(getActivity(), "Specify course timings for the day.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Select course day under course schedule.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Select an instructor for the course.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please enter course title.", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e) {

                }
            }
        });

        displayInstructorsForCourse();
        super.onActivityCreated(savedInstanceState);
    }

    void displayInstructorsForCourse() {

        List<Instructor> instructorList = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        instructorList = realm.where(Instructor.class).equalTo("userName", currentUser).findAll();
        if(instructorList.size() == 0) {
            noInstructors.setVisibility(View.VISIBLE);
            create.setEnabled(false);
        } else {
            noInstructors.setVisibility(View.INVISIBLE);
            create.setEnabled(true);
            mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.card_view);
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new InstructorCourseAdapter((MainActivity) getActivity(), instructorList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_course, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateCourse.OnFragmentInteractionListener) {
            mListener = (CreateCourse.OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void saveCourse(Course c);
        int getSelectedInstructorId();
    }
}
