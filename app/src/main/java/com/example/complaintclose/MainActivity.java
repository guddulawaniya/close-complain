package com.example.complaintclose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.complaintclose.Adapters.Tabs_complaintsAdapter;
import com.example.complaintclose.javafiles.NetworkUtils;
import com.example.complaintclose.loginpages.login_Actvity;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView profile = findViewById(R.id.profile);
        TextView clientid = findViewById(R.id.clientid);
        TextView profiledetails = findViewById(R.id.profiledetails);
        TextView status = findViewById(R.id.status);
        TextView close_complain = findViewById(R.id.close_complain);
        TextView termandcondition = findViewById(R.id.termandcondition);
        LinearLayout profilelayout = findViewById(R.id.profilelayout);


        profiledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        profilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Status_activity.class);
                startActivity(intent);
            }
        });
        close_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Status_activity.class);
                startActivity(intent);
            }
        });
        termandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, webview_activity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerlayout = findViewById(R.id.drawerlayout);

                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });

        CardView floatingbutton = findViewById(R.id.floatingbutton);
        TabLayout tablayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new Tabs_complaintsAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
        SharedPreferences preferences = getSharedPreferences("postdata", MODE_PRIVATE);
        String mobilenumber = preferences.getString("number", null);

        clientid.setText("Client ID : " + mobilenumber);

        TextView logoutbutton = findViewById(R.id.logoutbutton);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor preferences = getSharedPreferences("logindata", MODE_PRIVATE).edit();
                preferences.clear();
                preferences.commit();
                Intent intent = new Intent(MainActivity.this, login_Actvity.class);
                startActivity(intent);
                finish();
            }
        });

        floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, close_complaint_Form_Activity.class);
                intent.putExtra("id", 1);
                startActivity(intent);

            }
        });


    }


}