package com.example.complaintclose;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclose.Adapters.data_insert_module;
import com.example.complaintclose.Adapters.datashowAdapter;
import com.example.complaintclose.Adapters.datashowmodule;
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
    TextView  notfound;

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
        notfound = findViewById(R.id.notfound);
       ImageView backarrow = findViewById(R.id.backarrow);
      notfound.setVisibility(View.GONE);
        Intent intent = getIntent();
        String complainid = intent.getStringExtra("id");

        postdataonlygroupapi(complainid);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void postdataonlygroupapi(String complainnumber) {
        mProgressDialog.show();

        String registrationURL = config_file.Base_url+"get_item_details_close.php?complaint_id="+complainnumber;
        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                mProgressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i=0;i< jsonArray.length();i++)
                    {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String groupname = object.getString("group_name");
                        String item_name = object.getString("item_name");
                        String item_qty = object.getString("item_qty");
                        String item_srno = object.getString("item_srno");
                        list.add(new datashowmodule(groupname,item_name,item_qty,item_srno));

                    }
                    if (!list.isEmpty())
                    {
                        recyclerview.setLayoutManager(new LinearLayoutManager(data_show.this));
                        datashowAdapter adapter = new datashowAdapter(list,data_show.this);
                        recyclerview.setAdapter(adapter);

                    }
                    else
                    {
                        notfound.setVisibility(View.VISIBLE);

                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
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