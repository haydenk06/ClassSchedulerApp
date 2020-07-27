package com.example.kelseyhaydenc196;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.Model.Assessment;

import java.util.ArrayList;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder> {
    private List<Assessment> assessmentList = new ArrayList<>();
    private AssessmentAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentHolder holder, int position) {
        Assessment currentAssessment = assessmentList.get(position);
        holder.textViewAssessmentTitle.setText(currentAssessment.getAssessmentTitle());
        holder.textViewAssessmentType.setText(currentAssessment.getAssessmentType());
    }

    @Override
    public int getItemCount() {
        return assessmentList.size();
    }

    public void setAssessmentList(List<Assessment> assessmentList) {
        this.assessmentList = assessmentList;
        notifyDataSetChanged();
    }

    /////getting assessment to outside, swipe delete function /////
    public Assessment getAssessmentAt(int position) {
        return assessmentList.get(position);
    }

    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewAssessmentTitle;
        private TextView textViewAssessmentType;

        public AssessmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssessmentTitle = itemView.findViewById(R.id.text_view_assessment_title);
            textViewAssessmentType = itemView.findViewById(R.id.text_view_assessment_type);

            ///// for clicking on assessment item /////
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                ///// check for invalid position
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(assessmentList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Assessment assessment);
    }

    public void setOnItemClickListener(AssessmentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
