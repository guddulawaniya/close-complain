package com.example.complaintclose.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.R;
import com.example.complaintclose.complain_details_activity;

import java.util.ArrayList;

public class datashowAdapter extends RecyclerView.Adapter<datashowAdapter.viewholder> {

    ArrayList<datashowmodule> list;
    Context context;

    public datashowAdapter(ArrayList list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_series_layout_list, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        datashowmodule module = list.get(position);
        holder.qntyno.setText(module.getQntyno());
        holder.serialNo.setText(module.getSerialNo());

        holder.itemName.setText(module.getItemName());
        holder.groupname.setText(module.getGroupname());
        holder.indexing.setText(""+position+1);

//        if (!module.getItemName().isEmpty())
//        {
//            holder.indexing.setText(position+1);
//        }


        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView qntyno, serialNo,indexing;
        ImageView deletebutton;
        AutoCompleteTextView itemName,groupname;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            groupname = itemView.findViewById(R.id.groupname);
            qntyno = itemView.findViewById(R.id.qntyno);
            serialNo = itemView.findViewById(R.id.serialNo);
            indexing = itemView.findViewById(R.id.indexing);
            deletebutton = itemView.findViewById(R.id.deletebutton);
        }
    }
}

