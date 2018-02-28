package com.example.monic.coursemanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by monic on 11/5/2017.
 */

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {

    List<Instructor> mData = new ArrayList<>();

    private static int counter=0;
    static MainActivity mainActivity;

    public AddAdapter(MainActivity mainActivity, List<Instructor> mData) {
        this.mData = mData;
        this.mainActivity = mainActivity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView instructorName;
        TextView instructorEmail;
        ImageView professorImage;
        Instructor instructorData;

        public ViewHolder(View v) {
            super(v);
            instructorName = (TextView) v.findViewById(R.id.instructorName);
            instructorEmail = (TextView) v.findViewById(R.id.instructorEmail);
            professorImage = (ImageView) v.findViewById(R.id.profImage);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InstructorInterface listener = (InstructorInterface) mainActivity;
                    listener.displayInstructorDetails(instructorData);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle("Do you want to delete this instructor?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    realm.where(Course.class).equalTo("instructorId", instructorData.getId())
                                            .equalTo("userName", instructorData.getUserName()).findAll().deleteAllFromRealm();
                                    realm.commitTransaction();

                                    realm.beginTransaction();
                                    realm.where(Instructor.class).equalTo("id", instructorData.getId()).findAll().deleteAllFromRealm();
                                    realm.commitTransaction();
                                    InstructorInterface listener = (InstructorInterface) mainActivity;
                                    listener.refreshPage();
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
                .inflate(R.layout.instructor_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Instructor i = mData.get(position);
        holder.instructorData = i;
        holder.instructorName.setText(i.getFirstName() + i.getLastName());
        holder.instructorEmail.setText(i.getEmailId());
        if(i.getImageUri() != null)
            holder.professorImage.setImageURI(i.getImageUri());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface InstructorInterface {
        void displayInstructorDetails(Instructor c);
        void refreshPage();
    }

}

