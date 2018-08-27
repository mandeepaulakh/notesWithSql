package com.example.android2.noteswithsql.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android2.noteswithsql.R;
import com.example.android2.noteswithsql.model.notesData;

import java.util.ArrayList;

/**
 * Created by mandeep on 4/9/17.
 */

public class getNotesAdapter extends RecyclerView.Adapter<getNotesAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    public ArrayList<notesData> studentList;
    private ArrayList<notesData> studentListFiltered;
    private EvidenceAdapterListener listener;
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView noticeTitle,noticeDate;
                public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            noticeTitle = view.findViewById(R.id.noticeTitle);
            noticeDate = view.findViewById(R.id.noticeDate);
            cardView = itemView.findViewById(R.id.card_view);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (int) v.getTag();

                    listener.onEvidenceSelected(studentListFiltered.get(getAdapterPosition()));

                }
            });

        }
    }


    public getNotesAdapter(Context mContext, ArrayList<notesData> studentList, EvidenceAdapterListener listener) {
        this.mContext = mContext;
        this.studentList = studentList;
        this.studentListFiltered = studentList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_notice, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        notesData noticeData = studentListFiltered.get(position);
        holder.cardView.setTag(position);
        holder.noticeTitle.setText(noticeData.getNote_text());
        if (noticeData.getNote_date().equals("")){
            holder.noticeDate.setText("");
        }else {
            holder.noticeDate.setText(noticeData.getNote_date());

        }

    }


    @Override
    public int getItemCount() {
        return studentListFiltered.size();
    }

    public notesData getItem(int position){
        return studentList.get(position);
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    studentListFiltered = studentList;
                } else {
                    ArrayList<notesData> filteredList = new ArrayList<>();
                    for (notesData row : studentList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNote_text().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    studentListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = studentListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                studentListFiltered = (ArrayList<notesData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface EvidenceAdapterListener {
        void onEvidenceSelected(notesData getStudentData);
    }

}
