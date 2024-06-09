package com.example.complaintclose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.complaintclose.loginpages.login_Actvity;


public class Profile_activity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);


        TextView clientid = findViewById(R.id.clientid);
        TextView termcondition = findViewById(R.id.termcondition);
        TextView logoutbutton = findViewById(R.id.logoutbutton);
        ImageView backbutton = findViewById(R.id.backbutton);
        TextView status = findViewById(R.id.status);
        TextView close_complain = findViewById(R.id.close_complain);
        close_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_activity.this, Status_activity.class);
                startActivity(intent);
            }
        });  status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_activity.this, Status_activity.class);
                startActivity(intent);
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor preferences = getSharedPreferences("postdata",MODE_PRIVATE).edit();
                preferences.clear();
                preferences.commit();
                Intent intent = new Intent(Profile_activity.this, login_Actvity.class);
                startActivity(intent);
                finish();
            }
        });

        termcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_activity.this, webview_activity.class);
                startActivity(intent);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences preferences = getSharedPreferences("postdata", MODE_PRIVATE);
        String mobilenumber = preferences.getString("number", null);
        clientid.setText(mobilenumber);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile_activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}