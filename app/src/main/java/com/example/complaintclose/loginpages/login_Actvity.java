package com.example.complaintclose.loginpages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.complaintclose.R;
import com.example.complaintclose.javafiles.config_file;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login_Actvity extends AppCompatActivity {


    ProgressDialog mProgressDialog;
    TextInputEditText mobilenumber;
    TextInputLayout mobilenumberlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actvity);


         mobilenumber = findViewById(R.id.mobilenumber);
         mobilenumberlayout = findViewById(R.id.setpasswordlayout);

        Button loginbutton = findViewById(R.id.loginbutton);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Login");
        mProgressDialog.setMessage("Please Wait");

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobilenumber.getText().toString().isEmpty() && mobilenumber.getText().length()==10)
                {

                    Intent intent = new Intent(login_Actvity.this, OTP_verify.class);
                    intent.putExtra("otp","1234");
                    intent.putExtra("number",mobilenumber.getText().toString());
                    startActivity(intent);
                    finish();


                }
                else
                {
                    mobilenumberlayout.startAnimation(AnimationUtils.loadAnimation(getApplication(),R.anim.shake_text));
                    mobilenumberlayout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
                    mobilenumberlayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
                    mobilenumberlayout.setError("Enter the mobile number");
                    mobilenumber.requestFocus();
                }
            }
        });
    }

    private void loginapi() {

        String url = config_file.Base_url+"login.php";
        mProgressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(login_Actvity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                mProgressDialog.dismiss();


                try {

                    JSONArray respObj = new JSONArray(response);
                    JSONObject object = respObj.getJSONObject(0);

                    int id = object.getInt("id");
                    String type = object.getString("type");
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String phone = object.getString("phone");
                    String password = object.getString("password");
                    SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                    editor.putInt("userid",id);
                    editor.commit();

                    Intent intent = new Intent(login_Actvity.this, OTP_verify.class);
                    intent.putExtra("otp","1234");
                    intent.putExtra("number",mobilenumber.getText().toString());
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(login_Actvity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("number", mobilenumber.getText().toString());

                return params;
            }
        };
        queue.add(request);
    }
}