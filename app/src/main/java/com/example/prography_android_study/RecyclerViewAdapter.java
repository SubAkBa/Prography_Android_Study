package com.example.prography_android_study;

import android.util.TypedValue;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerViewItem> itemlist;
    private OnItemClickListener rlistener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.rlistener = listener ;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;

        public RecyclerViewHolder(View view){
            super(view);

            this.title = (TextView)view.findViewById(R.id.viewtitle);
            this.date = (TextView)view.findViewById(R.id.viewdate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        RecyclerViewItem item = itemlist.get(pos);

                        if (rlistener != null) {
                            rlistener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }
    }

    public RecyclerViewAdapter(ArrayList<RecyclerViewItem> itemlist){
        this.itemlist = itemlist;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memoviewlayout, parent, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewholder, int position) {
        viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.title.setGravity(Gravity.CENTER);
        viewholder.date.setGravity(Gravity.CENTER);

        viewholder.title.setText(itemlist.get(position).getTitle());
        viewholder.date.setText(itemlist.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return (null != this.itemlist ? this.itemlist.size() : 0);
    }

}
