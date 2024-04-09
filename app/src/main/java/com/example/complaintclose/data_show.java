package com.example.complaintclose;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclose.Adapters.data_insert_module;
import com.example.complaintclose.Adapters.datashowAdapter;
import com.example.complaintclose.Adapters.datashowmodule;
import com.example.complaintclose.javafiles.NetworkUtils;
import com.example.complaintclose.javafiles.config_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class data_show extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    ArrayList<datashowmodule> list;

    RecyclerView recyclerview;
    ConstraintLayout nointernet;
    SwipeRefreshLayout swipeRefreshLayout;
    String complainid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);

        // loader dialog box
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please Wait");
        mProgressDialog.setMessage("Loading..");
        list = new ArrayList<>();

         recyclerview = findViewById(R.id.recyclerview);
        nointernet = findViewById(R.id.nointernet);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

       ImageView backarrow = findViewById(R.id.backarrow);
        Intent intent = getIntent();
         complainid = intent.getStringExtra("id");

        postdataonlygroupapi(complainid);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();

        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void refreshData() {
        new android.os.Handler().postDelayed(() -> {

         postdataonlygroupapi(complainid);
            swipeRefreshLayout.setRefreshing(false);
        }, 2000); // 2000 milliseconds = 2 seconds (adjust as needed)
    }
    private void postdataonlygroupapi(String complainnumber) {
        mProgressDialog.show();
        nointernet.setVisibility(View.GONE);
        list.clear();

        if (!NetworkUtils.isNetworkAvailable(this)) {
            mProgressDialog.dismiss();
            recyclerview.setVisibility(View.GONE);
            nointernet.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String registrationURL = config_file.Base_url+"get_item_details_close.php?complaint_id="+complainnumber;
//        String registrationURL = "https://dummy-crm.raghaw.in/api/get_item_details_close.php?complaint_id=SM20248";
        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                mProgressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    boolean status = jsonObject.getBoolean("status");
                    if (status)
                    {
                        JSONArray Dataarray = jsonObject.getJSONArray("data");
                        Log.d("dataarray",Dataarray.toString());
                        for (int i=0;i< Dataarray.length();i++)
                        {
                            JSONObject object = Dataarray.getJSONObject(i);
                            String groupname = object.getString("group_name");
                            String item_name = object.getString("item_name");
                            String item_qty = object.getString("item_qty");
                            String item_srno = object.getString("item_srno");
                            int item_id = object.getInt("id");
                            list.add(new datashowmodule(item_id,groupname,item_name,item_qty,item_srno));
                            recyclerview.setLayoutManager(new LinearLayoutManager(data_show.this));
                            datashowAdapter adapter = new datashowAdapter(list,data_show.this);
                            recyclerview.setAdapter(adapter);

                        }
                    }


                } catch (JSONException e) {
                    Toast.makeText(data_show.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(secound_update_activity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();


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