package com.example.complaintclose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.complaintclose.Adapters.complaintModule;
import com.example.complaintclose.Adapters.detail_Adapter;
import com.example.complaintclose.Sqlite_Files.Citynamedb;

import java.util.ArrayList;

public class complain_details_activity extends AppCompatActivity {

    Citynamedb citynamedb;
    Cursor cursor;
    String cityname ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_inner_layout);

        ArrayList<complaintModule> list  = new ArrayList<>();

        citynamedb = new Citynamedb(this);
        cursor = citynamedb.getdata();





        Intent intent = getIntent();
        String complainno = intent.getStringExtra("comlainno");
        String status = String.valueOf(intent.getIntExtra("status", 0));
        String createdate = intent.getStringExtra("date");
        String createtime = intent.getStringExtra("time");
        String emailid = intent.getStringExtra("emailid");
        String mobileno = intent.getStringExtra("mobileno");
        String partycode = intent.getStringExtra("partycode");
        String brandname = intent.getStringExtra(" brand");
        String partyname = intent.getStringExtra("partyname");
        String complainreason = intent.getStringExtra("complainreason");
        String city = intent.getStringExtra("city");
        String state = intent.getStringExtra("state");
        String country = intent.getStringExtra("country");
        String address = intent.getStringExtra("address");

        SharedPreferences.Editor editor = getSharedPreferences("postdata",MODE_PRIVATE).edit();
        editor.putString("email",emailid);
        editor.putString("mobile",mobileno);
        editor.commit();




        RecyclerView datarecyclerview = findViewById(R.id.datarecyclerview);

        ImageView backArrow = findViewById(R.id.backarrow);
        Button okaybutton = findViewById(R.id.okaybutton);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        okaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(complain_details_activity.this,close_complaint_Form_Activity.class);
                intent1.putExtra("id",2);
                intent1.putExtra("complainno",complainno);
                startActivity(intent1);


            }
        });
        if (status != null && !status.equals("null") && !status.isEmpty()) {
            list.add(new complaintModule("Status", status));
        }

        if (createdate != null && !createdate.equals("null") && !createdate.isEmpty()) {
            list.add(new complaintModule("Create Date", createdate));
        }
        if (createtime != null && !createtime.equals("null") && !createtime.isEmpty()) {
            list.add(new complaintModule("Create Time", createtime));
        }
        if (complainno != null && !complainno.equals("null") && !complainno.isEmpty()) {
            list.add(new complaintModule("Complain NO", complainno));
        }
        if (partycode != null && !partycode.equals("null") && !partycode.isEmpty()) {
            list.add(new complaintModule("Party Code", partycode));
        }
        if (partyname != null && !partyname.equals("null") && !partyname.isEmpty()) {
            list.add(new complaintModule("Party Name", partyname));
        }
        if (brandname != null && !brandname.equals("null") && !brandname.isEmpty()) {
            list.add(new complaintModule("Brand Name", brandname));
        }
        if (emailid != null && !emailid.equals("null") && !emailid.isEmpty()) {
            list.add(new complaintModule("Email Id", emailid));
        }
        if (mobileno != null && !mobileno.equals("null") && !mobileno.isEmpty()) {
            list.add(new complaintModule("Mobile No", mobileno));
        }
        if (complainreason != null && !complainreason.equals("null")  && !complainreason.isEmpty()) {
            list.add(new complaintModule("Discripation", complainreason));
        }
        if (city != null && !city.equals("null") && !city.isEmpty()) {
            list.add(new complaintModule("City", city));
        }
        if (state != null && !state.equals("null") && !state.isEmpty()) {
            list.add(new complaintModule("State", state));
        }
        if (country != null && !country.equals("null") && !country.isEmpty()) {
            list.add(new complaintModule("Country", country));
        }

        if (address != null && !address.equals("null") && !address.isEmpty()) {
            list.add(new complaintModule("Address", address));
        }

        datarecyclerview.setLayoutManager(new LinearLayoutManager(this));
        detail_Adapter detailAdapter = new detail_Adapter(list,complain_details_activity.this);
        datarecyclerview.setAdapter(detailAdapter);



    }
}