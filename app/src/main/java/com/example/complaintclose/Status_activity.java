package com.example.complaintclose;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.complaintclose.Adapters.complaintAdapter;
import com.example.complaintclose.Adapters.complaintModule;
import com.example.complaintclose.Sqlite_Files.Citynamedb;
import com.example.complaintclose.Sqlite_Files.countrynamedb;
import com.example.complaintclose.Sqlite_Files.partynamedb;
import com.example.complaintclose.javafiles.config_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Status_activity extends AppCompatActivity {


    RecyclerView recyclerView;

    ArrayList<complaintModule> list;
    LinearLayout progressBar;
    partynamedb databaseManager;
    Citynamedb citynamedb;
    countrynamedb countrydb;


    Cursor cursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_status);


        progressBar = findViewById(R.id.shimmereffect_layout);
        recyclerView = findViewById(R.id.statusRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // connection sqlite database
        databaseManager = new partynamedb(this);
        countrydb = new countrynamedb(this);
        citynamedb = new Citynamedb(this);

        progressBar.setVisibility(View.GONE);
        list = new ArrayList<>();

        logindata();

    }

    private void logindata() {
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
                        int status = object1.getInt("status");
                        String compliant_no = object1.getString("compliant_no");

                        if (status == 0 && !compliant_no.isEmpty()) {

                            int party_id = object1.getInt("party_id");
                            String date = object1.getString("create_date");
                            String address = object1.getString("address");

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


                            list.add(new complaintModule(compliant_no, date, partyname, address, status));

                            complaintAdapter adapter = new complaintAdapter(list, Status_activity.this);
                            recyclerView.setAdapter(adapter);

                        }

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


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