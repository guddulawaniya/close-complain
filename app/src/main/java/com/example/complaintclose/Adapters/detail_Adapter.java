package com.example.complaintclose.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.R;
import com.example.complaintclose.Sqlite_Files.Citynamedb;
import com.example.complaintclose.Sqlite_Files.brandnamedb;
import com.example.complaintclose.Sqlite_Files.countrynamedb;
import com.example.complaintclose.Sqlite_Files.partynamedb;
import com.example.complaintclose.Sqlite_Files.statenamedb;
import com.example.complaintclose.javafiles.fetchdata_from_sqlite;

import java.util.ArrayList;

public class detail_Adapter extends RecyclerView.Adapter<detail_Adapter.viewholder> {

    ArrayList<complaintModule> list;
    Context context;

    fetchdata_from_sqlite fetchdata;

    public detail_Adapter(ArrayList<complaintModule> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_recyclerview_card, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        complaintModule module = list.get(position);
        holder.staticText.setText(module.getStaticText());
        fetchdata = new fetchdata_from_sqlite(context);


        if (module.getDynamicText().length() > 1) {
            holder.dynamicText.setText(module.getDynamicText());
        } else if (!module.getDynamicText().isEmpty() && module.getStaticText().equals("Status")) {
            int completeid = Integer.parseInt(module.getDynamicText());

            if (completeid == 0) {
                holder.dynamicText.setText("Pending");
            } else if (completeid == 2) {
                holder.dynamicText.setText("Closed");
            } else if (completeid == 1) {
                holder.dynamicText.setText("Approved");

            } else if (completeid == 3) {
                holder.dynamicText.setText("Rajected");
            }

        } else if (module.getStaticText().equals("City")) {
            holder.dynamicText.setText(
                    fetchdata.citysetdynamicdata(
                            module.getDynamicText()));

        } else if (!module.getDynamicText().isEmpty() && module.getStaticText().equals("Party Name")) {

            holder.dynamicText.setText(
                    fetchdata.partysetdynamicdata(
                            module.getDynamicText()));

        } else if (!module.getDynamicText().isEmpty() && module.getStaticText().equals("Brand Name")) {
            holder.dynamicText.setText(
                    fetchdata.brandsetdynamicdata(
                            module.getDynamicText()));
        } else if (!module.getDynamicText().isEmpty() && module.getStaticText().equals("State")) {
            holder.dynamicText.setText(
                    fetchdata.statesetdynamicdata(
                            module.getDynamicText()));

        } else if (!module.getDynamicText().isEmpty() && module.getStaticText().equals("Country")) {
            holder.dynamicText.setText(
                    fetchdata.countrysetdynamicdata(
                            module.getDynamicText()));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView staticText, dynamicText;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            staticText = itemView.findViewById(R.id.staticText);
            dynamicText = itemView.findViewById(R.id.dynamicText);
        }
    }

}

