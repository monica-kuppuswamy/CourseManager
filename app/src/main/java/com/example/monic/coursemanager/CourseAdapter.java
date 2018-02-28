package com.example.monic.coursemanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by monic on 11/5/2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    List<Course> mData = new ArrayList<>();
    Realm realm;

    private static int counter=0;
    static MainActivity mainActivity;

    public CourseAdapter(MainActivity mainActivity, List<Course> mData) {
        this.mData = mData;
        this.mainActivity = mainActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        TextView courseTitle;
        TextView courseInstructor;
        TextView courseSchedule;
        Course courseData;

        public ViewHolder(View v) {
            super(v);
            courseTitle = (TextView) v.findViewById(R.id.courseTitle);
            courseInstructor = (TextView) v.findViewById(R.id.courseInstructor);
            courseSchedule = (TextView) v.findViewById(R.id.courseSchedule);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseInterface listener = (CourseInterface) mainActivity;
                    listener.displayCourseDetails(courseData);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle("Do you want to delete this course?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    realm.where(Course.class).equalTo("id", courseData.getId()).findAll().deleteAllFromRealm();
                                    realm.commitTransaction();
                                    CourseInterface listener = (CourseInterface) mainActivity;
                                    listener.refreshCoursePage();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Course course = mData.get(position);
        holder.courseData = course;
        realm = Realm.getDefaultInstance();
        RealmResults<Instructor> i = realm.where(Instructor.class).equalTo("id", course.getInstructorId()).findAll();
        holder.courseTitle.setText(course.getTitle());
        holder.courseInstructor.setText(i.get(0).getFirstName() + " " + i.get(0).getLastName());
        holder.courseSchedule.setText(course.getDay() + " " + course.getTime());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface CourseInterface {
        void displayCourseDetails(Course c);
        void refreshCoursePage();
    }
}

