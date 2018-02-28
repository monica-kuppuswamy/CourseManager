package com.example.monic.coursemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.monic.coursemanager.R.*;

/**
 * Created by monic on 11/5/2017.
 */

public class InstructorCourseAdapter extends RecyclerView.Adapter<InstructorCourseAdapter.ViewHolder> {

    List<Instructor> mData = new ArrayList<>();
    private int lastCheckedPosition = -1;
    private AdapterListener mListener;

    private static int counter=0;
    static MainActivity mainActivity;

    public InstructorCourseAdapter(MainActivity mainActivity, List<Instructor> mData) {
        this.mData = mData;
        this.mainActivity = mainActivity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        TextView instructorName;
        RadioButton radioButton;
        ImageView image;
        public ViewHolder(View v) {
            super(v);
            instructorName = (TextView) v.findViewById(id.instructorName);
            radioButton = (RadioButton) v.findViewById(id.radioButton);
            image = (ImageView) v.findViewById(id.instructorImage);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    mListener = (AdapterListener) mainActivity;
                    mListener.setInstructorId(lastCheckedPosition);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout.instructor_for_course, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Instructor i = mData.get(position);
        holder.instructorName.setText(i.getFirstName() + " " + i.getLastName());
        if(i.getImageUri() != null) {
            holder.image.setImageURI(i.getImageUri());
        }
        holder.radioButton.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface AdapterListener {
        void setInstructorId(int id);
    }

}

