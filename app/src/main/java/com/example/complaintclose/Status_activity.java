package com.example.complaintclose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.Adapters.complaintAdapter;
import com.example.complaintclose.Adapters.complaintModule;
import com.example.complaintclose.Fragments.current_complaint_Fragment;
import com.example.complaintclose.Sqlite_Files.Citynamedb;
import com.example.complaintclose.Sqlite_Files.countrynamedb;
import com.example.complaintclose.Sqlite_Files.partynamedb;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Status_activity extends AppCompatActivity {


    RecyclerView recyclerView;

    List<complaintModule> list;
    LinearLayout progressBar;
    partynamedb databaseManager;
    Citynamedb citynamedb;
    countrynamedb countrydb;


    Cursor cursor;
    boolean id;
    ConstraintLayout nodata;
    ConstraintLayout animationView;
    TextView titletext;
    ImageView backarrow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_status);


        backarrow = findViewById(R.id.backarrow);
        animationView = findViewById(R.id.nointernet);
        titletext = findViewById(R.id.titletext);
        animationView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.shimmereffect_layout);
        recyclerView = findViewById(R.id.statusRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // connection sqlite database
        databaseManager = new partynamedb(this);
        countrydb = new countrynamedb(this);
        citynamedb = new Citynamedb(this);
        Intent intent = getIntent();
         id = intent.getBooleanExtra("id",false);
         if (id)
         {
             getallData();
             titletext.setText("Close Complaints");
         }
         else
         {
             getcomplaindataRefrsh();
             titletext.setText("Status");

         }

        progressBar.setVisibility(View.GONE);
        list = new ArrayList<>();


        complaintAdapter adapter = new complaintAdapter(list, Status_activity.this);
        recyclerView.setAdapter(adapter);

    }

    private void getcomplaindataRefrsh() {


        if (!NetworkUtils.isNetworkAvailable(this)) {
            animationView.setVisibility(View.VISIBLE);
            return;
        }

        String registrationURL = config_file.Base_url + "getcomplaint.php";


        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {
                list.clear();

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    boolean statuscheck = jsonObject.getBoolean("status");

                    if (!statuscheck) {
                        nodata.setVisibility(View.VISIBLE);
                        return;
                    }

                    JSONArray object = jsonObject.getJSONArray("data");
                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        int status = object1.getInt("status");
                        String compliant_no = object1.getString("compliant_no");

                        if (status == 0 && !compliant_no.isEmpty()) {
                            int party_id = object1.getInt("party_id");
                            String createDate = object1.getString("create_date");
                            String createtime = object1.getString("create_time");
                            String emailid = object1.getString("email");
                            String address = object1.getString("address");
                            String mobileno = object1.getString("phone");
                            String brand = object1.getString("brand_id");
                            String partycode = object1.getString("party_code");
                            String complainreason = object1.getString("complaint");
                            String city = object1.getString("city_id");
                            String state = object1.getString("state");
                            String country = object1.getString("country");
                            String tdsin = object1.getString("tds_in");
                            String tdsout = object1.getString("tds_out");

                            cursor = databaseManager.getdata();
                            String partyname = null;

                            if (cursor != null && cursor.moveToNext()) {
                                do {
                                    int userId = cursor.getInt(0);
                                    if (userId == party_id) {
                                        partyname = cursor.getString(1);
                                    }
                                } while (cursor.moveToNext());
                                cursor.close();
                            }

                            list.add(new complaintModule(compliant_no, createDate, createtime, partyname, address, emailid, mobileno, brand,
                                    partycode, complainreason, city, state, country,
                                    status,tdsin,tdsout));

                            Collections.sort(list, new Comparator<complaintModule>() {
                                @Override
                                public int compare(complaintModule module1, complaintModule module2) {
                                    return module1.getCompliant_no().substring(2).compareTo(module2.getCompliant_no().substring(2));
                                }
                            });
                            Collections.reverse(list);
                            complaintAdapter adapter = new complaintAdapter(list, Status_activity.this);
                            recyclerView.setAdapter(adapter);

                        }

                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(Status_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
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
    private void getallData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);
        if (!NetworkUtils.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("postdata", MODE_PRIVATE);
        String mobile = preferences.getString("number", null);


        String registrationURL = config_file.Base_url + "getcomplaint.php";


        class registration extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    boolean statuscheck = jsonObject.getBoolean("status");

                    if (!statuscheck)
                    {
                        nodata.setVisibility(View.VISIBLE);
                        return;
                    }
                    JSONArray object = jsonObject.getJSONArray("data");

                    for (int i = 0; i < object.length(); ++i) {

                        JSONObject object1 = object.getJSONObject(i);
                        String compliant_no = object1.getString("compliant_no");
                        int status = object1.getInt("status");

                        if (!compliant_no.isEmpty() && status==2) {

                            int party_id = object1.getInt("party_id");
                            String createDate = object1.getString("create_date");
                            String createtime = object1.getString("create_time");
                            String emailid = object1.getString("email");
                            String address = object1.getString("address");
                            String mobileno = object1.getString("phone");
                            String brand = object1.getString("brand_id");
                            String partycode = object1.getString("party_code");
                            String complainreason = object1.getString("complaint");
                            String city = object1.getString("city_id");
                            String state = object1.getString("state");
                            String country = object1.getString("country");
                            String tdsin = object1.getString("tds_in");
                            String tdsout = object1.getString("tds_out");

                            cursor = databaseManager.getdata();
                            String partyname = null;

                            if (cursor != null && cursor.moveToNext()) {
                                do {
                                    int userId = cursor.getInt(0);
                                    if (userId == party_id) {
                                        partyname = cursor.getString(1);
                                    }
                                } while (cursor.moveToNext());
                                cursor.close();
                            }

                            list.add(new complaintModule(compliant_no, createDate, createtime, partyname, address, emailid, mobileno, brand,
                                    partycode, complainreason, city, state, country
                                    , status,tdsin,tdsout));

                            complaintAdapter adapter = new complaintAdapter(list, Status_activity.this);
                            recyclerView.setAdapter(adapter);

                        }


                    }


                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    throw new RuntimeException(e);

                }
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