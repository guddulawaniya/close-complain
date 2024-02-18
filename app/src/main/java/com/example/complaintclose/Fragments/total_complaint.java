package com.example.complaintclose.Fragments;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.complaintclose.Adapters.complaintAdapter;
import com.example.complaintclose.R;
import com.example.complaintclose.Adapters.complaintModule;
import com.example.complaintclose.Sqlite_Files.Insertdata_partyname_sqlite;
import com.example.complaintclose.Sqlite_Files.partynamedb;
import com.example.complaintclose.javafiles.InternetConnection;
import com.example.complaintclose.javafiles.config_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class total_complaint extends Fragment {

    ArrayList<complaintModule> list;
    RecyclerView recyclerView;
    LinearLayout progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    InternetConnection internetConnection;
    partynamedb databaseManager;
    Insertdata_partyname_sqlite insertdatatablefile;

    Cursor cursor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_total_complaint, container, false);
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.totalcomplaintRecyclerview);
        progressBar = view.findViewById(R.id.shimmereffect_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // internet connnection
        internetConnection = new InternetConnection(getContext());

        // connection sqlite database
        databaseManager = new partynamedb(getContext());

        // set partyname data by ids

        insertdatatablefile = new Insertdata_partyname_sqlite(getContext());


        // Close the database when done
        databaseManager.close();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (internetConnection.isConnected())
            {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                refreshData();
            }else
            {
                recyclerView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.VISIBLE);
            }


        });

        progressBar.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                internetConnection = new InternetConnection(getContext());
            }
        }, 2000);

        if (internetConnection.isConnected())
        {
            progressBar.setVisibility(View.GONE);
            getallData();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }
    private void refreshData() {
        new android.os.Handler().postDelayed(() -> {
            getallData();

            swipeRefreshLayout.setRefreshing(false);
        }, 2000); // 2000 milliseconds = 2 seconds (adjust as needed)
    }
    private void getallData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        String registrationURL = config_file.Base_url + "getcomplaint.php";


        class registration extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONArray object = new JSONArray(s);


                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        String compliant_no = object1.getString("compliant_no");
                        int status = object1.getInt("status");

                        if (!compliant_no.isEmpty() && compliant_no!=null)
                        {

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

                            list.add(new complaintModule(compliant_no, createDate,createtime, partyname, address, emailid,mobileno,brand,
                                    partycode,complainreason , city , state , country
                                    , status ));

                            complaintAdapter adapter = new complaintAdapter(list, getContext());
                            recyclerView.setAdapter(adapter);

                        }


                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

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