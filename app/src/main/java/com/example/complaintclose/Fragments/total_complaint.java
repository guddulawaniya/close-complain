package com.example.complaintclose.Fragments;

import static com.example.complaintclose.Fragments.current_complaint_Fragment.CHECK_REFRESH;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.complaintclose.Sqlite_Files.Insertdata_partyname_sqlite;
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


public class total_complaint extends Fragment {

    List<complaintModule> list;
    RecyclerView recyclerView;
    LinearLayout progressBar;
    partynamedb databaseManager;
    Insertdata_partyname_sqlite insertdatatablefile;
    SwipeRefreshLayout swipeRefreshLayout;

    Cursor cursor;
    ConstraintLayout animationView;
    ConstraintLayout nofounddata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_complaint, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        animationView = view.findViewById(R.id.nointernet);
        animationView.setVisibility(View.GONE);
        nofounddata = view.findViewById(R.id.nofounddata);


        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.totalcomplaintRecyclerview);
        progressBar = view.findViewById(R.id.shimmereffect_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.GONE);


        // connection sqlite database
        databaseManager = new partynamedb(getContext());

        // set partyname data by ids

        insertdatatablefile = new Insertdata_partyname_sqlite(getContext());


        // Close the database when done
        databaseManager.close();

        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();

        });

//        current_complaint_Fragment.SharedPreferencesLoader sharedPreferencesLoader = new current_complaint_Fragment.SharedPreferencesLoader(getContext());
//        list = sharedPreferencesLoader.loadInBackground();
        if (!list.isEmpty())
        {
            animationView.setVisibility(View.GONE);
            complaintAdapter adapter = new complaintAdapter(list, getContext());
            recyclerView.setAdapter(adapter);
            if (NetworkUtils.isNetworkAvailable(getContext()))
            {
                getcomplaindataRefrsh();
            }

        }else getallData();


        return view;
    }

    private void getcomplaindataRefrsh() {

        if (!NetworkUtils.isNetworkAvailable(getContext())) {
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
                        nofounddata.setVisibility(View.VISIBLE);
                        return;
                    }

                    JSONArray object = jsonObject.getJSONArray("data");
                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        int status = object1.getInt("status");
                        String compliant_no = object1.getString("compliant_no");

                        if (status != 0 && !compliant_no.isEmpty()) {
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

                            Toast.makeText(getContext() , ""+status, Toast.LENGTH_SHORT).show();

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
                            complaintAdapter adapter = new complaintAdapter(list, getContext());
                            recyclerView.setAdapter(adapter);


                        }

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
    private void refreshData() {
        new android.os.Handler().postDelayed(() -> {

            getallData();
            swipeRefreshLayout.setRefreshing(false);
        }, 2000); // 2000 milliseconds = 2 seconds (adjust as needed)
    }

    private void getallData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        animationView.setVisibility(View.GONE);

        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            animationView.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getContext().getSharedPreferences("postdata", getContext().MODE_PRIVATE);
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
                        nofounddata.setVisibility(View.VISIBLE);
                        return;
                    }
                    JSONArray object = jsonObject.getJSONArray("data");

                    for (int i = 0; i < object.length(); ++i) {

                        JSONObject object1 = object.getJSONObject(i);
                        String compliant_no = object1.getString("compliant_no");
                        int status = object1.getInt("status");

                        if (!compliant_no.isEmpty() && status!=0) {

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

                            complaintAdapter adapter = new complaintAdapter(list, getContext());
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

    @Override
    public void onResume() {
        super.onResume();

        if (CHECK_REFRESH)
        {
            getcomplaindataRefrsh();
        }
    }

}