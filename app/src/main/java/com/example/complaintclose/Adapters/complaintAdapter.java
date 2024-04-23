package com.example.complaintclose.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.R;
import com.example.complaintclose.complain_details_activity;

import java.util.ArrayList;
import java.util.List;

public class complaintAdapter extends RecyclerView.Adapter<complaintAdapter.viewholder> {

    List<complaintModule> list;
    Context context;


    public complaintAdapter(List list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complain_status_card, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        complaintModule module = list.get(position);
        holder.comaplaintno.setText(module.getCompliant_no());
        holder.date.setText(module.getCreateDate());
        holder.address.setText(module.getAddress());
        holder.partyname.setText(module.getPartyname());
        int completeid = module.getStatus();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= context.getSharedPreferences("postdata", Context.MODE_PRIVATE).edit();
                editor.putString("index", String.valueOf(holder.getAbsoluteAdapterPosition()));
                editor.commit();
                Intent intent = new Intent(context, complain_details_activity.class);
                intent.putExtra("comlainno", module.getCompliant_no());
                intent.putExtra("status", module.getStatus());
                intent.putExtra("date", module.getCreateDate());
                intent.putExtra("time", module.getCreatetime());
                intent.putExtra("emailid", module.getEmailid());
                intent.putExtra("mobileno", module.getMobileno());
                intent.putExtra("partycode", module.getPartycode());
                intent.putExtra("brand", module.getBrand());
                intent.putExtra("partyname", module.getPartyname());
                intent.putExtra("complainreason", module.getComplainreason());
                intent.putExtra("city", module.getCity());
                intent.putExtra("state", module.getState());
                intent.putExtra("country", module.getCountry());
                intent.putExtra("tdsin", module.getTdsin());
                intent.putExtra("tdsout", module.getTdsout());
                intent.putExtra("address", module.getAddress());

                context.startActivity(intent);

            }
        });


        if (completeid == 2) {
            holder.statusicon.setImageResource(R.drawable.approved_icon);
            holder.status.setBackgroundColor(Color.parseColor("#1EA323"));
            holder.status.setText("Closed");
        } else if (completeid == 3) {
            holder.statusicon.setImageResource(R.drawable.raject_icon);
            holder.status.setBackgroundColor(Color.parseColor("#D11414"));
            holder.status.setText("Rajected");

        }else if (completeid == 1) {
            holder.statusicon.setImageResource(R.drawable.approve);
            holder.status.setBackgroundColor(Color.parseColor("#0CA5AC"));
            holder.status.setText("Approved");

        } else if (completeid==0){
            holder.statusicon.setImageResource(R.drawable.pening_icon);
            holder.status.setBackgroundColor(Color.parseColor("#FFD041"));
            holder.status.setText("Pending");

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView comaplaintno, partyname, date, address, status;
        ImageView statusicon;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            comaplaintno = itemView.findViewById(R.id.complainnumber);
            partyname = itemView.findViewById(R.id.complaintNo);
            date = itemView.findViewById(R.id.date);
            address = itemView.findViewById(R.id.address);
            status = itemView.findViewById(R.id.status);
            statusicon = itemView.findViewById(R.id.statusicon);
        }
    }
}
