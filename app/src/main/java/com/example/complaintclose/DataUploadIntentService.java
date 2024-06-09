package com.example.complaintclose;

// DataUploadIntentService.java
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complaintclose.javafiles.ApiService;
import com.example.complaintclose.javafiles.RetrofitClient;
import com.example.complaintclose.javafiles.config_file;
import com.example.complaintclose.javafiles.datapostmodule;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class DataUploadIntentService extends IntentService {

    NotificationHelper notificationHelper;
    datapostmodule datapostmodule;
    String encodeImageString;
    boolean updateNotification = false;


    public DataUploadIntentService() {
        super("DataUploadIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            notificationHelper = new NotificationHelper(this);
            encodeImageString = intent.getStringExtra("image");
             datapostmodule = (datapostmodule) intent.getSerializableExtra("datamodule");
            uploaddatatodb();
            textdatapost();
        }
    }

    private void uploaddatatodb() {
//        notificationHelper.showProgressNotification(DataUploadIntentService.this, "Upload Complain");
        StringRequest request = new StringRequest(Request.Method.POST, config_file.Base_url + "imageupload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        senddataonserver();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("upload", encodeImageString);
                map.put("complainid", datapostmodule.getComplainnumber());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    void notificationdata(){
        if (updateNotification)
        {
            notificationHelper.updateprogressbar(DataUploadIntentService.this, "Successfully Close Complaint");
        }

    }
 private void textdatapost() {
//        notificationHelper.showProgressNotification(DataUploadIntentService.this, "Upload Complain");
        StringRequest request = new StringRequest(Request.Method.POST, config_file.Base_url + "closecomplaintthird.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateNotification = true;
                        notificationdata();
//                        senddataonserver();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("complainnumber", datapostmodule.getComplainnumber());
                map.put("compliantid", datapostmodule.getCompliantid());
                map.put("party_code", datapostmodule.getParty_code());
                map.put("party_id", datapostmodule.getParty_id());
                map.put("brand_name", datapostmodule.getBrand_name());
                map.put("cityid", datapostmodule.getCityid());
                map.put("state", datapostmodule.getState());
                map.put("email", datapostmodule.getEmail());
                map.put("description", datapostmodule.getDescription());
                map.put("phone", datapostmodule.getPhone());
                map.put("tdsin", datapostmodule.getTdsin());
                map.put("tdsout", datapostmodule.getTdsout());
                map.put("address", datapostmodule.getAddress());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
