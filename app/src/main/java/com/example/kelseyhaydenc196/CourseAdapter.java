package com.example.kelseyhaydenc196;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.Model.Course;

import java.util.ArrayList;
import java.util.List;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    private List<Course> courseList = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {
        Course currentCourse = courseList.get(position);
        holder.textViewTitle.setText(currentCourse.getCourseTitle());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    /////getting course to outside, swipe delete function/////
    public Course getCourseAt(int position) {
        return courseList.get(position);
    }


    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);

            /////for clicking on course item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    /////check for invalid position
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(courseList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    /////  spinner used for drop down list of terms /////
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
