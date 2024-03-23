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
        }
    }

    private void uploaddatatodb() {
        notificationHelper.showProgressNotification(DataUploadIntentService.this, "Upload Complain");
        StringRequest request = new StringRequest(Request.Method.POST, config_file.Base_url + "imageupload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        senddataonserver();
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
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void senddataonserver() {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<Void> call = apiService.sendDataArray(datapostmodule);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    // Do something on success
                    notificationHelper.updateprogressbar(DataUploadIntentService.this, "Upload Complain");
                    Log.d("DataUploadIntentService", "Successfully Close Complaint");
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
