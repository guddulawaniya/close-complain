package com.example.complaintclose.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.R;
import com.example.complaintclose.complain_details_activity;
import com.example.complaintclose.data_show;
import com.example.complaintclose.javafiles.config_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class datashowAdapter extends RecyclerView.Adapter<datashowAdapter.viewholder> {

    ArrayList<datashowmodule> list;
    Context context;
    ProgressDialog mProgressDialog;

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
        int index = position;
        index++;
        holder.indexing.setText(""+index);

        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteitem(holder.getAdapterPosition());
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

            itemName.setEnabled(false);
            groupname.setEnabled(false);
            qntyno.setEnabled(false);
            serialNo.setEnabled(false);
        }
    }

    private void deleteitem(int deleteindex) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Please Wait");
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.show();

        String registrationURL = config_file.Base_url+"delete_item.php?deleteindex="+deleteindex;
        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                mProgressDialog.dismiss();
                list.remove(deleteindex);
                notifyItemRemoved(deleteindex);

                Toast.makeText(context, "delete item Successfully", Toast.LENGTH_SHORT).show();

                super.onPostExecute(s);

            }

            @Override
            protected String doInBackground(String... param) {

                try {
                    URL url = new URL(param[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    return br.readLine();
                } catch (Exception ex) {
                    return ex.getMessage();
                }

            }

        }
        registration obj = new registration();
        obj.execute(registrationURL);
    }
}

