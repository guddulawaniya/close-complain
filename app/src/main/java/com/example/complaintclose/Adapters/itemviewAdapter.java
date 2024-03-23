package com.example.complaintclose.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.R;
import com.example.complaintclose.Roomdatabase.notes;

import java.util.List;

public class itemviewAdapter extends RecyclerView.Adapter<itemviewAdapter.viewholder> {
    List<notes> list;

    public itemviewAdapter(List<notes> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public itemviewAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewcard_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemviewAdapter.viewholder holder, int position) {
        notes note = list.get(position);
        holder.textView.setText(position+1+" "+note.getText());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView textView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemname);
        }
    }
}
