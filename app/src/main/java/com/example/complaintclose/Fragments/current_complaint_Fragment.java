package com.example.complaintclose.Fragments;

import android.content.Context;
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
import androidx.loader.content.AsyncTaskLoader;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class current_complaint_Fragment extends Fragment  {

    List<complaintModule> list;
    RecyclerView recyclerView;

    LinearLayout progressBar;
    partynamedb databaseManager;
    Citynamedb citynamedb;
    countrynamedb countrydb;

    SwipeRefreshLayout swipeRefreshLayout;

    Cursor cursor;
    ConstraintLayout nodata;
    ConstraintLayout animationView;

    public static boolean CHECK_REFRESH = false;

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
        nodata = view.findViewById(R.id.nofounddata);

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
        SharedPreferencesLoader sharedPreferencesLoader = new SharedPreferencesLoader(getContext());
        list = sharedPreferencesLoader.loadInBackground();
        if (!list.isEmpty())
        {
            animationView.setVisibility(View.GONE);
            complaintAdapter adapter = new complaintAdapter(list, getContext());
            recyclerView.setAdapter(adapter);
            if (NetworkUtils.isNetworkAvailable(getContext()))
            {
                getcomplaindataRefrsh();
            }

        }else getcomplaindata();


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

                            Collections.sort(list, new Comparator<complaintModule>() {
                                @Override
                                public int compare(complaintModule module1, complaintModule module2) {
                                    return module1.getCompliant_no().substring(2).compareTo(module2.getCompliant_no().substring(2));
                                }
                            });
                            Collections.reverse(list);
                            complaintAdapter adapter = new complaintAdapter(list, getContext());
                            recyclerView.setAdapter(adapter);


                            storedata();
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
    public static class SharedPreferencesLoader extends AsyncTaskLoader<List<complaintModule>> {

        public SharedPreferencesLoader(Context context) {
            super(context);
        }

        @Override
        public List<complaintModule> loadInBackground() {
            // Retrieve data from SharedPreferences
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("listdata", Context.MODE_PRIVATE);
            String json = sharedPreferences.getString("list", null);
            Type type = new TypeToken<List<complaintModule>>() {}.getType();
            Gson gson = new Gson();
            List<complaintModule> dataList = gson.fromJson(json, type);
            return dataList != null ? dataList : new ArrayList<>();
        }
    }

    private void refreshData() {
        new android.os.Handler().postDelayed(() -> {

            SharedPreferences.Editor sharedPreferences = getContext().getSharedPreferences("listdata", Context.MODE_PRIVATE).edit();
            sharedPreferences.clear();
            sharedPreferences.commit();
            getActivity().recreate();
            swipeRefreshLayout.setRefreshing(false);
        }, 2000); // 2000 milliseconds = 2 seconds (adjust as needed)
    }

    private void getcomplaindata() {
        animationView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
            if (!NetworkUtils.isNetworkAvailable(getContext())) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                animationView.setVisibility(View.VISIBLE);
//                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
        String registrationURL = config_file.Base_url + "getcomplaint.php";
        SharedPreferences preferences = getContext().getSharedPreferences("postdata", getContext().MODE_PRIVATE);
        String mobile = preferences.getString("number", null);

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

                            Gson gson = new Gson();
                            String json = gson.toJson(list);

                            // Save the JSON string in SharedPreferences
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("listdata", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("list", json);
                            editor.apply();

                            storedata();

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

    void storedata()
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        // Save the JSON string in SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("listdata", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("list", json);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (CHECK_REFRESH) {
            getcomplaindataRefrsh();
        }
    }

}
