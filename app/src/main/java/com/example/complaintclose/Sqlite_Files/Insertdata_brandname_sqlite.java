package com.example.complaintclose.Sqlite_Files;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.complaintclose.javafiles.InternetConnection;
import com.example.complaintclose.javafiles.config_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Insertdata_brandname_sqlite {
    Context context;
    brandnamedb databaseManager;
    InternetConnection internetConnection;

    public Insertdata_brandname_sqlite(Context context) {
        this.context = context;
        insertdatatable();
    }

    public void insertdatatable() {

        databaseManager = new brandnamedb(context);
        String RegistrationURL = config_file.Base_url + "brand.php";
        internetConnection = new InternetConnection(context);
        class registration extends AsyncTask<String, String, String> {


            @Override
            protected void onPostExecute(String s) {

                databaseManager.deleteTable();
                try {
                    JSONArray object = new JSONArray(s);
                    int i = 0;

                    while (i < object.length()) {
                        JSONObject object1 = object.getJSONObject(i);
                        String partyname = object1.getString("name");
                        databaseManager.insertdata(partyname);
                        ++i;
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();

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
        if (internetConnection.isConnected()) {
            registration obj = new registration();
            obj.execute(RegistrationURL);
        } else {
            Toast.makeText(context, "Check your internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}