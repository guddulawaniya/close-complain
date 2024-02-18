package com.example.complaintclose;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complaintclose.javafiles.config_file;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class secound_update_activity extends AppCompatActivity {

    CardView itemcard;
    Button prebutton, submitbutton;
    AutoCompleteTextView groupname,itemName;
    TextInputEditText serialNo,qntyno;
    ImageView backarrow;
    LinearLayout addlinearlayout;
    AlertDialog alertDialog;
    List<String> grouplist,itemlist;
    TextView deletebutton;
    String encodeImageString;
    ProgressDialog mProgressDialog;

    String complainno,createdate,createtime,emailid,mobileno,partyid,tdsin,tdsout,partycode,descripation,brand,address,cityid,state,country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secount_activity_layout);

        TextView addbutton = findViewById(R.id.additembutton);
        deletebutton = findViewById(R.id.deletebutton);
        prebutton = findViewById(R.id.prebutton);
        submitbutton = findViewById(R.id.saveButton);
        backarrow = findViewById(R.id.backarrow);
        addlinearlayout = findViewById(R.id.addlinearlayout);
        grouplist = new ArrayList<>();
        itemlist = new ArrayList<>();

        Intent intent = getIntent();
        complainno = intent.getStringExtra("complainno");
        createdate = intent.getStringExtra("createdate");
        createtime = intent.getStringExtra("createtime");
        partycode = intent.getStringExtra("partycode");
        descripation = intent.getStringExtra("descripation");
        brand = intent.getStringExtra("brand");
        address = intent.getStringExtra("address");
        cityid = intent.getStringExtra("cityid");
        state = intent.getStringExtra("state");
        partyid = intent.getStringExtra("partyid");
        country = intent.getStringExtra("country");
        state = intent.getStringExtra("state");
        country = intent.getStringExtra("country");
        tdsin = intent.getStringExtra("TDS_IN");
        tdsout = intent.getStringExtra("TDS_out");
        encodeImageString = intent.getStringExtra("encodeImageString");

        SharedPreferences preferences = getSharedPreferences("postdata",MODE_PRIVATE);
         emailid = preferences.getString("email",null);
         mobileno = preferences.getString("mobile",null);

        getdropdowndata(config_file.Base_url+"getgroupname.php",grouplist);
        getdropdowndata(config_file.Base_url+"getitemname.php",itemlist);

         // inflate the dynamic layout of card
        additemfor_series_number();


        AlertDialog.Builder builder = new AlertDialog.Builder(secound_update_activity.this);

        builder.setMessage("Thank you for Feedback We are try to resolve your problem very soon \n your feedback is valueble for us ");

        builder.setTitle("Complain");

        builder.setCancelable(false);


        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            Intent intent1 = new Intent(secound_update_activity.this,MainActivity.class);
            (secound_update_activity.this).finish();
            intent1.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent1);

        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please Wait");
        mProgressDialog.setMessage("Loading..");

        alertDialog = builder.create();


        int lastIndex = addlinearlayout.getChildCount();
        if (lastIndex < 1) deletebutton.setVisibility(View.GONE);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteitem_for_series_number();

            }
        });
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaddatatodb();

            }
        });

        prebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additemfor_series_number();
            }
        });

    }

    private void animateDuplicateView(View view) {
        // ViewPropertyAnimator example (translation animation)
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );

        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }


    void additemfor_series_number() {
        View view = getLayoutInflater().inflate(R.layout.add_series_layout_list, null, false);


        groupname = view.findViewById(R.id.groupname);
        itemName = view.findViewById(R.id.itemName);

        ArrayAdapter<String> groupadapter = new ArrayAdapter<>(this, R.layout.list_layout, grouplist);
        groupname.setDropDownBackgroundResource(R.color.dialog_bg);
        groupname.setAdapter(groupadapter);

        ArrayAdapter<String> itemadapter = new ArrayAdapter<>(this, R.layout.list_layout, itemlist);
        itemName.setDropDownBackgroundResource(R.color.dialog_bg);
        itemName.setAdapter(itemadapter);

        animateDuplicateView(view);
        if (deletebutton.getVisibility() == View.GONE) {
            deletebutton.setVisibility(View.VISIBLE);
        }
        addlinearlayout.addView(view);

    }

    void deleteitem_for_series_number() {

        int lastIndex = addlinearlayout.getChildCount()-1;
        addlinearlayout.removeViewAt(lastIndex);
        if (lastIndex < 1) deletebutton.setVisibility(View.GONE);
    }

    private void getdropdowndata(String registrationURL, List<String> list) {

        class registration extends AsyncTask<String, String, String> {
            @Override
            protected void onPostExecute(String s) {

                try {
                    JSONArray object = new JSONArray(s);


                    for (int i = 0; i < object.length(); ++i) {
                        JSONObject object1 = object.getJSONObject(i);
                        String brandname = object1.getString("name");
                        list.add(brandname);
                    }

                } catch (JSONException e) {
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
    private void postDataUsingVolley() {
        String url = config_file.Base_url + "close_compalint_id.php";

        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        LocalDate dates = LocalDate.now();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = dateFormat.format(currentDate);


        RequestQueue queue = Volley.newRequestQueue(secound_update_activity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();
                Toast.makeText(secound_update_activity.this, "successfully Added Complaint", Toast.LENGTH_SHORT).show();
                alertDialog.show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(secound_update_activity.this, "Something want wrong  ", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("compliantnumber", complainno);
                params.put("date_s", createdate);
                params.put("time_s", createtime);
                params.put("party_id", partyid);
                params.put("brand_name", brand);
                params.put("party_code", partycode);
                params.put("party_address", address);
                params.put("cityid", cityid);
                params.put("state", state);
                params.put("country", country);
                params.put("email", emailid);
                params.put("phone", mobileno);
                params.put("description", descripation);
                params.put("TDS_IN", tdsin);
                params.put("TDS_OUT", tdsout);
                params.put("DATE", String.valueOf(dates));
                params.put("TIME", currentTime);
                return params;
            }
        };
        queue.add(request);
    }
    private void uploaddatatodb()
    {

        mProgressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, config_file.Base_url+"imageupload.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                postDataUsingVolley();
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map=new HashMap<String, String>();
                map.put("upload",encodeImageString);
                return map;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
