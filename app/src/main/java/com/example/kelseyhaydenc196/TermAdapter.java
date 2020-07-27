package com.example.kelseyhaydenc196;

import android.database.Observable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Term;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {
    private static List<Term> termList = new ArrayList<>();
    private TermAdapter.OnItemClickListener listener;

    public static void setOnClickListener(TermAdapter.OnItemClickListener onItemClickListener) {
    }

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermHolder holder, int position) {
        Term currentTerm = termList.get(position);
        holder.textViewTermTitle.setText(currentTerm.getTermTitle());
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
        notifyDataSetChanged();
    }

    //getting course to outside, swipe delete function
    public static Term getTermAt(int position) {
        return termList.get(position);
    }

    class TermHolder extends RecyclerView.ViewHolder {
        private TextView textViewTermTitle;

        public TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTermTitle = itemView.findViewById(R.id.text_view_term_title);

            //for clicking on term item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //check for invalid position
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(termList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Term term);
    }

    public void setOnItemClickListener(TermAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
