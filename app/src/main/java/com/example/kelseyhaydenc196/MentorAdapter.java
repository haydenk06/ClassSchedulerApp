package com.example.kelseyhaydenc196;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.Model.Mentor;

import java.util.ArrayList;
import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorHolder> {
    private List<Mentor> mentorList = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorAdapter.MentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorAdapter.MentorHolder holder, int position) {
        Mentor currentMentor = mentorList.get(position);
        holder.textViewName.setText(currentMentor.getName());
        holder.textViewPhoneNumber.setText(currentMentor.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    public void setMentorList(List<Mentor> mentors) {
        this.mentorList = mentors;
        notifyDataSetChanged();
    }

    /////getting course to outside, swipe delete function/////
    public Mentor getMentorAt(int position) {
        return mentorList.get(position);
    }

    class MentorHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewPhoneNumber;

        public MentorHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPhoneNumber =itemView.findViewById(R.id.text_view_phone_number);

            //for clicking on course item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                //check for invalid position
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(mentorList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Mentor mentor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
