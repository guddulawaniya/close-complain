package com.example.complaintclose.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.complaintclose.Adapters.complaintAdapter;
import com.example.complaintclose.Adapters.complaintModule;
import com.example.complaintclose.R;
import com.example.complaintclose.Sqlite_Files.Citynamedb;
import com.example.complaintclose.Sqlite_Files.Insertdata_Cityname_sqlite;
import com.example.complaintclose.Sqlite_Files.Insertdata_Countryname_sqlite;
import com.example.complaintclose.Sqlite_Files.Insertdata_brandname_sqlite;
import com.example.complaintclose.Sqlite_Files.Insertdata_partyname_sqlite;
import com.example.complaintclose.Sqlite_Files.Insertdata_statename_sqlite;
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


public class current_complaint_Fragment extends Fragment  {

    ArrayList<complaintModule> list;
    RecyclerView recyclerView;

    LinearLayout progressBar;
    partynamedb databaseManager;
    Citynamedb citynamedb;
    countrynamedb countrydb;

    SwipeRefreshLayout swipeRefreshLayout;

    Cursor cursor;
    TextView nodata;
    ConstraintLayout animationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_complaint_, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        animationView = view.findViewById(R.id.nointernet);
        animationView.setVisibility(View.GONE);


        // connection sqlite database
        databaseManager = new partynamedb(getContext());
        countrydb = new countrynamedb(getContext());
        citynamedb = new Citynamedb(getContext());
        nodata = view.findViewById(R.id.nodata);

        // cityname insert database
        new Insertdata_Cityname_sqlite(getContext());

        // set partyname data by ids
        new Insertdata_partyname_sqlite(getContext());

        // set country data by ids
        new Insertdata_Countryname_sqlite(getContext());

        // set state data by ids
        new Insertdata_statename_sqlite(getContext());
        // set state data by ids
        new Insertdata_brandname_sqlite(getContext());


        list = new ArrayList<>();
        progressBar = view.findViewById(R.id.shimmereffect_layout);
        recyclerView = view.findViewById(R.id.currentRecyclerviewComplaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();

        });

        progressBar.setVisibility(View.GONE);

        getcomplaindata();

        return view;
    }

    private void refreshData() {
        new android.os.Handler().postDelayed(() -> {

           getActivity().recreate();
            swipeRefreshLayout.setRefreshing(false);
        }, 2000); // 2000 milliseconds = 2 seconds (adjust as needed)
    }

    private void getcomplaindata() {
        animationView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

            if (!NetworkUtils.isNetworkAvailable(getContext())) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                animationView.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
        recyclerView.setVisibility(View.GONE);
        String registrationURL = config_file.Base_url + "getcomplaint.php";
        SharedPreferences preferences = getContext().getSharedPreferences("postdata", getContext().MODE_PRIVATE);
        String mobile = preferences.getString("number", null);

        class registration extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONArray object = new JSONArray(s);
                    if (object.length() < 1) {
                        nodata.setVisibility(View.VISIBLE);
                    }
                    nodata.setVisibility(View.GONE);
                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        int status = object1.getInt("status");
                        String compliant_no = object1.getString("compliant_no");

                        if ((status == 0 && !compliant_no.isEmpty())) {
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

                            list.add(new complaintModule(compliant_no, createDate, createtime, partyname, address, emailid, mobileno, brand,
                                    partycode, complainreason, city, state, country,
                                    status));
                            Collections.reverse(list);
                            complaintAdapter adapter = new complaintAdapter(list, getContext());
                            recyclerView.setAdapter(adapter);


                        }
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
